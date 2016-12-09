package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.DistrictService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictDao districtDao;

    @Override
    public Page<District> findByCondition(District district, Pageable pageable) {
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);
        if (district.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }
        if (StringUtils.isNotBlank(district.getName())) {
            e = e.withMatcher("name", g.contains());
        } else {
            district.setName(null);
        }
        if (district.getDisplay() != null) {
            e = e.withMatcher("display", g.exact());
        } else {
            district.setDisplay(null);
        }
        return districtDao.findAll(Example.of(district, e), pageable);
    }

    @Override
    public List<District> getIdNameList() {

        return this.districtDao.getIdNameList();
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
}
