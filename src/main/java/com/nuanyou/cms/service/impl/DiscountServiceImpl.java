package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CouponTemplateDao;
import com.nuanyou.cms.dao.DiscountDao;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.MerchantCardDao;
import com.nuanyou.cms.entity.Discount;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.enums.DiscountType;
import com.nuanyou.cms.model.DiscountQueryParam;
import com.nuanyou.cms.service.DiscountService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    UserService userService;

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

    @Override
    public Page<Discount> findDiscount (final DiscountQueryParam param, Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        return discountDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }

                if (param.id != null) {
                    predicate.add(cb.equal(root.get("id"), param.id));
                }

                if (StringUtils.isNotBlank(param.name)) {
                    predicate.add(cb.like(root.get("title"), "%" + param.name + "%"));
                }
                if (param.countryId != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), param.countryId));
                }
                if (param.cityId != null) {
                    predicate.add(cb.equal(root.get("city").get("id"), param.cityId));
                }
                if (param.type != null) {
                    predicate.add(cb.equal(root.get("type").get("value"), param.type));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

}