package com.nuanyou.cms.model;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.coupon.CouponMerchant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/27.
 */
public class CouponBatchVO {

    /**
     * 指定优惠券可选方案
     */
    private static final BigDecimal[][] PlanList_1 = new BigDecimal[4][2];

    /**
     * 通用优惠券可选方案
     */
    private static final BigDecimal[][] PlanList_2 = new BigDecimal[6][2];

    static {
        PlanList_1[1][0] = new BigDecimal(300);
        PlanList_1[1][1] = new BigDecimal(15);
        PlanList_1[2][0] = new BigDecimal(500);
        PlanList_1[2][1] = new BigDecimal(40);
        PlanList_1[3][0] = new BigDecimal(1000);
        PlanList_1[3][1] = new BigDecimal(80);

        PlanList_2[1][0] = new BigDecimal(100);
        PlanList_2[1][1] = new BigDecimal(5);
        PlanList_2[2][0] = new BigDecimal(200);
        PlanList_2[2][1] = new BigDecimal(10);
        PlanList_2[3][0] = new BigDecimal(300);
        PlanList_2[3][1] = new BigDecimal(15);
        PlanList_2[4][0] = new BigDecimal(500);
        PlanList_2[4][1] = new BigDecimal(25);
        PlanList_2[5][0] = new BigDecimal(1000);
        PlanList_2[5][1] = new BigDecimal(50);
    }

    /**
     * 1.指定优惠券，2.通用优惠券
     */
    private Integer type;

    /**
     * 优惠方案，可多选
     */
    private List<Integer> plans;

    /**
     * 优惠券介绍
     */
    private String title;

    /**
     * 商户信息，可多选
     * 格式id:name
     */
    private List<String> mchInfos;

    /**
     * 用户ID，以“;”分隔
     */
    private String userIdText;

    /**
     * 有效期，以月为单位
     */
    private Integer validTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getPlans() {
        return plans;
    }

    public void setPlans(List<Integer> plans) {
        this.plans = plans;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMchInfos() {
        return mchInfos;
    }

    public void setMchInfos(List<String> mchInfos) {
        this.mchInfos = mchInfos;
    }

    public String getUserIdText() {
        return userIdText;
    }

    public void setUserIdText(String userIdText) {
        this.userIdText = userIdText;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public List<Coupon> buildCoupon() {
        validate();
        String[] userIds = userIdText.split("\\;");

        List<Merchant> merchants = new ArrayList<>(mchInfos.size());
        for (String mchInfo : mchInfos) {
            String[] mchStr = mchInfo.split("\\:");
            Merchant merchant = new Merchant(NumberUtils.toLong(mchStr[0]), mchStr[1]);
            merchants.add(merchant);
        }

        Date validDate = calcValidTime();

        List<Coupon> couponList = new ArrayList<>(userIds.length * plans.size() * mchInfos.size());
        for (String userIdStr : userIds) {
            long userId = NumberUtils.toLong(userIdStr);
            for (Merchant mchInfo : merchants) {
                for (Integer plan : plans) {
                    Coupon coupon = genCoupon(userId, mchInfo, validDate, plan);
                    couponList.add(coupon);
                }
            }
        }
        return couponList;
    }

    private Coupon genCoupon(Long userId, Merchant mchInfo, Date validTime, Integer plan) {
        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setValidTime(validTime);
        coupon.setUseRange(2);
        coupon.setStatus(1);

        if (type == 1) {
            coupon.setStartPrice(PlanList_1[plan][0]);
            coupon.setPrice(PlanList_1[plan][1]);

            coupon.setGroupId(5L);
            coupon.setTitle(mchInfo.getName());
            coupon.setGroupName("特定商家优惠券");
            coupon.setIntro("点击查看商户详情");
            coupon.setAppointMerchant(true);

            CouponMerchant couponMerchant = new CouponMerchant();
            couponMerchant.setUserId(userId);
            couponMerchant.setMchId(mchInfo.getId());
            coupon.addCouponMerchant(couponMerchant);
        } else {
            coupon.setStartPrice(PlanList_2[plan][0]);
            coupon.setPrice(PlanList_2[plan][1]);

            coupon.setGroupId(7L);
            coupon.setGroupName("通用优惠券");
            coupon.setAppointMerchant(false);
            coupon.setTitle(this.title);
        }
        return coupon;
    }

    private Date calcValidTime() {
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

    private void validate() {
        if (plans == null || plans.isEmpty())
            throw new APIException(ResultCodes.MissingParameter, "未选择优惠方案");
        if (StringUtils.isBlank(userIdText))
            throw new APIException(ResultCodes.MissingParameter, "用户ID不能为空");
        if (validTime == null)
            throw new APIException(ResultCodes.MissingParameter, "有效期不能为空");
        if (type == 1) {
            if (mchInfos == null || mchInfos.isEmpty())
                throw new APIException(ResultCodes.MissingParameter, "未选择商家");
        }
    }

}