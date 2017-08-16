package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantCardDao;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantCard;
import com.nuanyou.cms.entity.SimpleMerchant;
import com.nuanyou.cms.entity.enums.CardType;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantCardService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class MerchantCardServiceImpl implements MerchantCardService {

    @Autowired
    private MerchantCardDao dao;
    @Autowired
    private UserService userService;

    @Override
    public MerchantCard saveNotNull(MerchantCard entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        MerchantCard oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

    public Page<MerchantCard> findByCondition(Integer index, final MerchantCard entity, final TimeCondition validTime) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return dao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                Merchant merchant = entity.getMerchant();
                if (merchant != null) {
                    Long mchId = merchant.getId();
                    if (mchId != null)
                        predicate.add(cb.equal(root.get("merchant").get("id").as(Long.class), mchId));
                }

                predicate.add(cb.equal(root.get("deleted"), entity.isDeleted()));

                Long id = entity.getId();
                if (id != null)
                    predicate.add(cb.equal(root.get("id").as(Long.class), id));

                String title = entity.getTitle();
                if (StringUtils.isNotBlank(title))
                    predicate.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));

                CardType type = entity.getType();
                if (type != null)
                    predicate.add(cb.equal(root.get("type").as(CardType.class), type));

                Boolean display = entity.getDisplay();
                if (display != null)
                    predicate.add(cb.equal(root.get("display").as(Boolean.class), display));

                if (validTime != null) {
                    if (validTime.getBegin() != null) {
                        Predicate p = cb.greaterThanOrEqualTo(root.get("validTime").as(Date.class), validTime.getBegin());
                        predicate.add(p);
                    }
                    if (validTime.getEnd() != null) {
                        Predicate p = cb.lessThanOrEqualTo(root.get("validTime").as(Date.class), validTime.getEnd());
                        predicate.add(p);
                    }
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public void delete(Long id) {
        MerchantCard entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            entity.setDeleted(true);
            dao.save(entity);
        }
    }

}