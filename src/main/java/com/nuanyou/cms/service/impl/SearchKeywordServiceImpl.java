package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.SearchKeywordDao;
import com.nuanyou.cms.entity.SearchKeyword;
import com.nuanyou.cms.entity.enums.RefundStatus;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.SearchKeywordService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/26.
 */
@Service
public class SearchKeywordServiceImpl implements SearchKeywordService {

    @Autowired
    private
    SearchKeywordDao searchKeywordDao;

    @Autowired
    private UserService userService;

    @Override
    public Page<SearchKeyword> findByCondition(int index, final SearchKeyword entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        final List<Long> countryIds = userService.findUserCountryId();
        return searchKeywordDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }
                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (entity.getDisplay() != null) {
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay()));
                }
                if (entity.getCountry() != null && entity.getCountry().getId() != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), entity.getCountry().getId()));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }

    @Override
    public SearchKeyword saveNotNull(SearchKeyword entity) {
        if (entity.getId() == null) {
            if(entity.getCity().getId()==null){
                entity.setCity(null);
            }
            return searchKeywordDao.save(entity);
        }
        SearchKeyword oldEntity = searchKeywordDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        if (oldEntity.getCity().getId()==null){
            oldEntity.setCity(null);
        }if(oldEntity.getCountry().getId()==null){
            oldEntity.setCountry(null);
        }
        return searchKeywordDao.save(oldEntity);
    }

}