package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CouponDao;
import com.nuanyou.cms.dao.CouponTemplateDao;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.coupon.CouponMerchant;
import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.entity.enums.CouponTemplateType;
import com.nuanyou.cms.entity.enums.UserRange;
import com.nuanyou.cms.model.CouponBatchVO;
import com.nuanyou.cms.model.CouponTemplateVO;
import com.nuanyou.cms.service.CouponTemplateService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/10/13.
 */
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Autowired
    private CouponDao couponDao;

    @Override
    public CouponTemplateVO saveNotNull(CouponTemplateVO vo) {
        if (vo.getType() != CouponTemplateType.Merchant)
            vo.setMerchants(null);
        CouponTemplate entity = BeanUtils.copyBean(vo, new CouponTemplate());
        entity = couponTemplateDao.save(entity);
        return BeanUtils.copyBean(entity, new CouponTemplateVO());
    }

    @Override
    public List<CouponTemplate> findIdNameList(CouponTemplateType type) {
        return couponTemplateDao.findIdNameList(type);
    }

    @Override
    public void batchSendCoupon(CouponBatchVO couponBatchVO) {
        List<Long> couponTemplateIds = couponBatchVO.getCouponTemplateIds();
        String userIdText = couponBatchVO.getUserIdText();
        Integer validTime = couponBatchVO.getValidTime();

        if (couponTemplateIds == null || couponTemplateIds.isEmpty())
            throw new APIException(ResultCodes.MissingParameter, "未选择优惠券模板");
        if (StringUtils.isBlank(userIdText))
            throw new APIException(ResultCodes.MissingParameter, "用户ID不能为空");
        if (validTime == null)
            throw new APIException(ResultCodes.MissingParameter, "有效期不能为空");

        String[] userIds = userIdText.split("\\;");

        Date validDate = calcValidTime(couponBatchVO.getValidTime());

        List<Coupon> couponList = new ArrayList<>(userIds.length * couponTemplateIds.size());
        for (Long templateId : couponTemplateIds) {
            CouponTemplate couponTemplate = couponTemplateDao.findOne(templateId);
            for (String userIdStr : userIds) {
                long userId = NumberUtils.toLong(userIdStr);
                Coupon coupon = genCoupon(userId, validDate, couponTemplate);
                couponList.add(coupon);
            }
        }

        couponDao.save(couponList);
    }


    private Coupon genCoupon(Long userId, Date validTime, CouponTemplate couponTemplate) {
        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setValidTime(validTime);
        coupon.setStatus(1);

        coupon.setPrice(couponTemplate.getPrice());
        coupon.setLocalPrice(couponTemplate.getLocalPrice());
        coupon.setStartPrice(couponTemplate.getStartPrice());
        coupon.setStartLocalPrice(couponTemplate.getStartLocalPrice());
        coupon.setUseRange(UserRange.toValue(couponTemplate.getUserRanges()));
        coupon.setTitle(couponTemplate.getTitle());
        coupon.setGroupId(couponTemplate.getCouponGroup().getId());
        coupon.setGroupName(couponTemplate.getCouponGroup().getName());
        coupon.setIntro(couponTemplate.getIntro());
        coupon.setCountryId(couponTemplate.getCountryId());

        coupon.setAppointMerchant(couponTemplate.getType() == CouponTemplateType.Merchant);
        if (couponTemplate.getType() == CouponTemplateType.Merchant) {
            List<String> merchants = couponTemplate.getMerchants();
            if (merchants == null || merchants.isEmpty())
                throw new APIException(ResultCodes.NotFoundMerchant);
            for (String mchIdStr : merchants) {
                long mchId = NumberUtils.toLong(mchIdStr);
                CouponMerchant couponMerchant = new CouponMerchant();
                couponMerchant.setUserId(userId);
                couponMerchant.setMchId(mchId);
                coupon.addCouponMerchant(couponMerchant);
            }
        }
        return coupon;
    }

    private static Date calcValidTime(int validTime) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        if (validTime == 1) {
            calender.add(Calendar.MONTH, 1);
        } else if (validTime == 2) {
            calender.add(Calendar.MONTH, 2);
        } else if (validTime == 3) {
            calender.add(Calendar.MONTH, 3);
        }
        return calender.getTime();
    }

}