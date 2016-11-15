package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CouponTemplateDao;
import com.nuanyou.cms.dao.DiscountDao;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.MerchantCardDao;
import com.nuanyou.cms.entity.Discount;
import com.nuanyou.cms.entity.enums.DiscountType;
import com.nuanyou.cms.service.DiscountService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Alan.ye on 2016/9/20.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Autowired
    private MerchantCardDao merchantCardDao;

    @Override
    public Discount saveNotNull(Discount entity) {
        Long itemId = entity.getRelatedId();
        validateId(entity.getType(), itemId);
        if (entity.getId() == null) {
            return discountDao.save(entity);
        }
        Discount oldEntity = discountDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return discountDao.save(oldEntity);
    }

    private void validateId(DiscountType type, Long id) {
        Object temp = null;
        if (DiscountType.Item == type) {
            temp = itemDao.findOne(id);
            if (temp == null)
                throw new APIException(ResultCodes.NotFoundItem);
        } else if (DiscountType.Discount == type) {
            temp = couponTemplateDao.findOne(id);
            if (temp == null)
                throw new APIException(ResultCodes.NotFoundCouponTemplate);
        } else if (DiscountType.Voucher == type) {
            temp = merchantCardDao.findOne(id);
            if (temp == null)
                throw new APIException(ResultCodes.NotFoundMerchantCard);
        }
    }

    @Override
    public void delete(Long id) {
        Discount entity = discountDao.findOne(id);
        if (entity != null) {
            entity.setDeleted(true);
            discountDao.save(entity);
        }
    }

}