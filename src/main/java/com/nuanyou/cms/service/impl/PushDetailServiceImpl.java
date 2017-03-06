package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
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
import com.nuanyou.cms.model.PushDetailListVo;
import com.nuanyou.cms.model.PushDetailVo;
import com.nuanyou.cms.service.PushDetailService;
import com.nuanyou.cms.util.BeanUtils;
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
            this.pushDetailDao.save(pushDetail);
        }
    }

    @Override
    public Page<PushDetailListVo> list(final PushDetailCondition pushDetailCondition, int index) {
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
                Predicate p = cb.equal(root.get("groupId"), pushDetailCondition.getGroupId());
                predicate.add(p);

                Predicate p1 = cb.equal(root.get("deleted"), false);
                predicate.add(p1);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);

        List<PushDetailListVo> pushDetailListVoList = new ArrayList<>();
        for (PushDetail pushDetail : page.getContent()) {
            PushDetailListVo pushDetailListVo = BeanUtils.copyBean(pushDetail, new PushDetailListVo());
            pushDetailListVo.setTypeName(PushDetailTypeEnum.getNameByType(pushDetail.getType()));

            if (pushDetail.getCountryId() == null || pushDetail.getCountryId() == 0) {
                pushDetailListVo.setCountryName("全部");
            } else {
                Country country = this.countryDao.findOne(pushDetail.getCountryId());
                if (country != null) {
                    pushDetailListVo.setCountryName(country.getName());
                }
            }
            if (pushDetail.getCityId() == null || pushDetail.getCityId() == 0) {
                pushDetailListVo.setCityName("全部");
            } else {
                City city = this.cityDao.findOne(pushDetail.getCityId());
                if (city != null) {
                    pushDetailListVo.setCityName(city.getName());
                }
            }
            if (pushDetail.getDistrictId() == null || pushDetail.getDistrictId() == 0) {
                pushDetailListVo.setDistrictName("全部");
            } else {
                District district = this.districtDao.findOne(pushDetail.getDistrictId());
                if (district != null) {
                    pushDetailListVo.setDistrictName(district.getName());
                }
            }
            if (pushDetail.getStatus() != null) {
                if (pushDetail.getStatus()) {
                    Date now = new Date();
                    if (pushDetail.getEndTime() != null && pushDetail.getEndTime().compareTo(now) < 0) {
                        pushDetailListVo.setStatusName("已过期");
                    } else {
                        pushDetailListVo.setStatusName("已启动");
                    }

                } else {
                    pushDetailListVo.setStatusName("已禁用");
                }
            }
            pushDetailListVoList.add(pushDetailListVo);
        }

        return new PageImpl<>(pushDetailListVoList, pageable, page.getTotalElements());
    }

    @Override
    public PushDetailVo findById(Long id) {
        PushDetail pushDetail = this.pushDetailDao.findOne(id);
        PushDetailVo pushDetailVo = BeanUtils.copyBean(pushDetail, new PushDetailVo());
        return pushDetailVo;
    }

    @Override
    public PushDetailVo update(PushDetailVo pushDetailVo) {
        PushDetail pushDetail = null;
        if (pushDetailVo.getId() != null) {
            pushDetail = this.pushDetailDao.findOne(pushDetailVo.getId());
            if (pushDetail != null) {
                pushDetail = BeanUtils.copyBeanNotNull(pushDetailVo, pushDetail);
            }
        } else {
            pushDetail = BeanUtils.copyBeanNotNull(pushDetailVo, new PushDetail());

        }
        this.pushDetailDao.save(pushDetail);
        pushDetailVo = BeanUtils.copyBean(pushDetail, pushDetailVo);
        return pushDetailVo;
    }

    @Override
    public void checkSource(Long id, String source) {
        Long num = this.pushDetailDao.countForCheckSource(id, source);
        if (num != null && num > 0) {
            throw new APIException(ResultCodes.SourceRepeat);
        }
    }
}
