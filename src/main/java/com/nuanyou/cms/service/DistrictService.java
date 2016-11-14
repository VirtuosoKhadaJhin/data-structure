package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.District;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface DistrictService {
    Page<District> findByCondition(Integer index, District district);

    List<District> getIdNameList();

    District saveNotNull(District entity);
}
