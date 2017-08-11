package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.DistrictVo;
import com.nuanyou.cms.service.DistrictService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictDao districtDao;
    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;
    @Autowired
    private UserService userService;

    @Override
    public Page<DistrictVo> findByCondition(final District entity, Pageable pageable, final Locale locale) {
        //edit at 2017/8/8 by young
        final List<Long> countryIds = userService.findUserCountryId();
        // 孙昊修改了查询方式, 在原有的字段sort上实现了排序功能
        Page<District> districts = districtDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("country").get("id").in(countryIds));
                }
                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getName())) {
                    predicate.add(cb.like(root.get("name"), "%" + entity.getName() + "%"));
                }
                if (null != entity.getDisplay()) {
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay().booleanValue()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("sort")), cb.desc(root.get("updatetime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        }, pageable);

        // keyCode结果集
        final List<String> keyCodes = Lists.newArrayList();

        // 根据截取中文和英文
        List<District> pageLists = districts.getContent();
        List<DistrictVo> districtVos = Lists.newArrayList();
        for (District dis : pageLists) {
            districtVos.add(convertToDistrictVo(dis));
        }

        if (keyCodes != null && keyCodes.size() > 0) {
            List<EntityNyLangsDictionary> dicts = dictionaryDao.findAll(new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();

                    predicate.add(root.get("keyCode").in(keyCodes));

                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });

            for (EntityNyLangsDictionary dict : dicts) {
                int keyCodeIndex = keyCodes.indexOf(dict.getKeyCode());

                DistrictVo districtVo = districtVos.get(keyCodeIndex);

                // 更新链表的甘油中英文的数据
                districtVos.set(keyCodeIndex, districtVo);
            }

            Page<DistrictVo> lists = new PageImpl<DistrictVo>(districtVos, pageable, districts.getTotalElements());
            return lists;
        }

        Page<DistrictVo> lists = new PageImpl<DistrictVo>(districtVos, pageable, districts.getTotalElements());
        return lists;
    }

    @Override
    public List<DistrictVo> findByCity(Long cityId) {
        if (cityId == null) {
            return this.convertToDistrictVos(districtDao.findAll());
        }
        List<District> districts = this.districtDao.findIdNameListByCityId(cityId);
        return this.convertToDistrictVos(districts);
    }

    @Override
    public List<District> getIdNameList() {
        List<Long> countryIds = userService.findUserCountryId();
        return this.districtDao.getIdNameList(true,countryIds);
    }

    @Override
    public List<District> getIdNameList(Long countryId) {
        return this.districtDao.getIdNameList(true,countryId);
    }

    @Override
    public District saveNotNull(District entity) {
        if (entity.getId() == null) {
            return districtDao.save(entity);
        }
        District oldEntity = districtDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return districtDao.save(oldEntity);
    }

    @Override
    public void updateDistrict(DistrictVo districtVo) {
        District district = BeanUtils.copyBeanNotNull(districtVo, new District());
        Date nowDate = new Date();
        if (districtVo.getId() == null) {
            district.setCreatetime(nowDate);
            district.setUpdatetime(nowDate);
            districtDao.save(district);
        } else {
            District oldEntity = districtDao.findOne(districtVo.getId());
            BeanUtils.copyBeanNotNull(district, oldEntity);
            oldEntity.setUpdatetime(nowDate);
            districtDao.save(oldEntity);
        }

    }

    private DistrictVo convertToDistrictVo(District entity) {
        DistrictVo districtVo = BeanUtils.copyBean(entity, new DistrictVo());
        return districtVo;
    }

    private List<DistrictVo> convertToDistrictVos(List<District> districts) {
        if (CollectionUtils.isEmpty(districts)) {
            return Lists.newArrayList();
        }
        List<DistrictVo> vos = Lists.newArrayList();
        for (District district : districts) {
            DistrictVo districtVo = BeanUtils.copyBean(district, new DistrictVo());
            vos.add(districtVo);
        }
        return vos;
    }
}
