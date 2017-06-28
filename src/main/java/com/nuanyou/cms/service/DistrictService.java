package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.model.DistrictVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;

/**
 * Created by Felix on 2016/9/7.
 */
public interface DistrictService {

    Page<DistrictVo> findByCondition(final District district, Pageable pageable, final Locale locale);

    List<District> getIdNameList();

    District saveNotNull(District entity);

    /**
     * 保存或者更新商圈
     *
     * @param districtVo
     */
    void updateDistrict(DistrictVo districtVo);
}