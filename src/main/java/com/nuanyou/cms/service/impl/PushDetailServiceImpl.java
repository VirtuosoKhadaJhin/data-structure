package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.dao.PushDetailDao;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.enums.PushDetailTypeEnum;
import com.nuanyou.cms.entity.push.PushDetail;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.PushDetailCondition;
import com.nuanyou.cms.model.PushDetailVo;
import com.nuanyou.cms.service.PushDetailService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * Created by yangkai on 2017/2/15.
 */
@Service
public class PushDetailServiceImpl implements PushDetailService {
    @Autowired
    private PushDetailDao pushDetailDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private DistrictDao districtDao;

    @Override
    public void deletePushDetail(Long id) {
        PushDetail pushDetail = this.pushDetailDao.findOne(id);
        if (pushDetail != null) {
            pushDetail.setDeleted(true);
            pushDetail.setUpdateTime(new Date());
            this.pushDetailDao.save(pushDetail);
        }
    }

    @Override
    public Page<PushDetailVo> list(final PushDetailCondition pushDetailCondition, int index) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        Page<PushDetail> page = pushDetailDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (pushDetailCondition.getCountryId() != null) {
                    Predicate p1 = cb.equal(root.get("countryId"), pushDetailCondition.getCountryId());
                    Predicate p2 = cb.equal(root.get("countryId"), 0);
                    Predicate p3 = cb.isNull(root.get("countryId"));
                    Predicate p = cb.or(p1, p2, p3);
                    predicate.add(p);
                }
                if (pushDetailCondition.getCityId() != null) {
                    Predicate p1 = cb.equal(root.get("cityId"), pushDetailCondition.getCityId());
                    Predicate p2 = cb.equal(root.get("cityId"), 0);
                    Predicate p3 = cb.isNull(root.get("cityId"));
                    Predicate p = cb.or(p1, p2, p3);
                    predicate.add(p);
                }
                if (pushDetailCondition.getStatus() != null) {
                    Predicate p = cb.equal(root.get("status"), pushDetailCondition.getStatus());
                    predicate.add(p);
                }
                Predicate p = cb.equal(root.get("pushGroup").get("id"), pushDetailCondition.getGroupId());
                predicate.add(p);

                Predicate p1 = cb.equal(root.get("deleted"), false);
                predicate.add(p1);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);

        List<PushDetailVo> pushDetailVoList = new ArrayList<>();
        for (PushDetail pushDetail : page.getContent()) {
            PushDetailVo pushDetailVo = BeanUtils.copyBean(pushDetail, new PushDetailVo());
            pushDetailVo.setTypeName(PushDetailTypeEnum.getNameByType(pushDetail.getType()));

            if (pushDetail.getCountryId() == null || pushDetail.getCountryId() == 0) {
                pushDetailVo.setCountryName("全部");
            } else {
                Country country = this.countryDao.findOne(pushDetail.getCountryId());
                if (country != null) {
                    pushDetailVo.setCountryName(country.getName());
                }
            }
            if (pushDetail.getCityId() == null || pushDetail.getCityId() == 0) {
                pushDetailVo.setCityName("全部");
            } else {
                City city = this.cityDao.findOne(pushDetail.getCityId());
                if (city != null) {
                    pushDetailVo.setCityName(city.getName());
                }
            }
            if (pushDetail.getDistrictId() == null || pushDetail.getDistrictId() == 0) {
                pushDetailVo.setDistrictName("全部");
            } else {
                District district = this.districtDao.findOne(pushDetail.getDistrictId());
                if (district != null) {
                    pushDetailVo.setDistrictName(district.getName());
                }
            }
            if (pushDetail.getStatus() != null) {
                if (pushDetail.getStatus()) {
                    pushDetailVo.setStatusName("已启动");
                } else {
                    pushDetailVo.setStatusName("已禁用");
                }
            }
            pushDetailVoList.add(pushDetailVo);
        }

        return new PageImpl<>(pushDetailVoList, pageable, page.getTotalElements());
    }
}
