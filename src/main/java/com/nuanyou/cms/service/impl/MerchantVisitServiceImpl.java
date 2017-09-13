package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantVisitDao;
import com.nuanyou.cms.dao.VisitTypeDao;
import com.nuanyou.cms.entity.MerchantVisit;
import com.nuanyou.cms.entity.VisitType;
import com.nuanyou.cms.entity.enums.MerchantCooperationStatus;
import com.nuanyou.cms.model.VisitQueryRequest;
import com.nuanyou.cms.service.MerchantVisitService;
import com.nuanyou.cms.service.UserService;
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
 * Created by young on 2017/9/1.
 */
@Service
public class MerchantVisitServiceImpl implements MerchantVisitService {

    @Autowired
    private UserService userService;
    @Autowired
    private MerchantVisitDao visitDao;
    @Autowired
    private VisitTypeDao visitTypeDao;

    @Override
    public Page<MerchantVisit> queryMerchantVisit(final VisitQueryRequest param,Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                if (param.getUserId() != null) {
                    predicate.add(cb.equal(root.get("user").get("id"), param.getUserId()));
                }
                if (param.getMchId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("id"), param.getMchId()));
                }
                if (StringUtils.isNotEmpty(param.getMchName())) {
                    predicate.add(cb.like(root.get("merchant").get("name"), "%"+param.getMchName()+"%"));
                }
                if (param.getCountryId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("country").get("id"), param.getCountryId()));
                }
                if (param.getCityId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("city").get("id"), param.getCityId()));
                }
                if (param.getDistrictId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("id"), param.getDistrictId()));
                }
                if (param.getVisitTypeId() != null) {
                    predicate.add(cb.equal(root.get("type"), param.getVisitTypeId()));
                }
                if (param.getStartTime()  != null) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("createTime"), param.getStartTime()));
                }
                if (param.getEndTime()  != null) {
                    predicate.add(cb.lessThanOrEqualTo(root.get("createTime"), param.getEndTime()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];

                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return visitDao.findAll(specification,pageable);
    }

    @Override
    public List<VisitType> findVisitTypes(){
        return visitTypeDao.findAll();
    }
}
