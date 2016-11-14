package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantCatDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantCatService;
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
 * Created by nuanyou on 2016/9/6.
 */
@Service
public class MerchantCatServiceImpl implements MerchantCatService {


    @Autowired
    private MerchantCatDao merchantCatDao;
    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Page<Merchant> findByCondition(final Integer mchid, final String name, Integer index, Integer pageSize) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        return merchantDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(name)) {
                    Predicate p1 = cb.like(root.get("mcat").get("name"), "%" + name + "%");
                    predicate.add(p1);
                }
                if (mchid != null) {
                    Predicate p2 = cb.equal(root.get("id"), mchid);
                    predicate.add(p2);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public void add(MerchantCat merchantCat) {
        merchantCatDao.save(merchantCat);
    }

    @Override
    public Page<MerchantCat> findByCondition(MerchantCat entity, Integer index) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);

        if (entity.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            e = e.withMatcher("name", g.contains());
        } else {
            entity.setName(null);
        }
        if (entity.getDisplay() != null) {
            e = e.withMatcher("display", g.exact());
        } else {
            entity.setDisplay(null);
        }
        return merchantCatDao.findAll(Example.of(entity, e), pageable);
    }

    @Override
    public List<MerchantCat> getIdNameList() {
        return this.merchantCatDao.getIdNameList();
    }

    @Override
    public MerchantCat saveNotNull(MerchantCat entity) {
        if (entity.getId() == null) {
            return merchantCatDao.save(entity);
        }
        MerchantCat oldEntity = merchantCatDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantCatDao.save(oldEntity);
    }
}
