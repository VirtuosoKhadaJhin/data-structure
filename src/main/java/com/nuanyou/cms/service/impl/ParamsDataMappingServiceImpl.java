package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ParamsDataMappingDao;
import com.nuanyou.cms.entity.user.ParamsDataMapping;
import com.nuanyou.cms.service.ParamsDataMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2017/6/9.
 */
@Service
public class ParamsDataMappingServiceImpl implements ParamsDataMappingService {



    @Autowired
    private ParamsDataMappingDao dataMappingDao;

    @Override
    public List<ParamsDataMapping> findAll() {
        return dataMappingDao.findAll();
    }
}
