package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.BannerDao;
import com.nuanyou.cms.entity.Banner;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.BannerService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
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
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerDao bannerDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<Banner> findByCondition(Integer index, final Banner entity) {

        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return bannerDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }
                if (!StringUtils.isEmpty(entity.getPage())) {
                    Predicate p = cb.equal(root.get("page"), entity.getPage());
                    predicate.add(p);
                }
                if (entity.getCountry() != null && entity.getCountry().getId() != null) {
                    Predicate p = cb.equal(root.get("country").get("id"), entity.getCountry().getId());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public Banner saveNotNull(Banner entity) {
        if (entity.getId() == null) {
            return bannerDao.save(entity);
        }
        Banner oldEntity = bannerDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        if (oldEntity.getCity().getId()==null){
            oldEntity.setCity(null);
        }if(oldEntity.getCountry().getId()==null){
            oldEntity.setCountry(null);
        }
        return bannerDao.save(oldEntity);
    }
}
