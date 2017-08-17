package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.entity.ItemCat;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemCatService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private UserService userService;

    @Override
    public Page<ItemCat> findByCondition(Integer index, final ItemCat entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return itemCatDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                if (entity.getName() != null) {
                    Predicate p = cb.equal(root.get("name"), entity.getName());
                    predicate.add(p);
                }
                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("id"), entity.getMerchant().getId());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public ItemCat saveNotNull(ItemCat entity) {
        if (entity.getId() == null) {
            return itemCatDao.save(entity);
        }
        ItemCat oldEntity = itemCatDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return itemCatDao.save(oldEntity);
    }
}
