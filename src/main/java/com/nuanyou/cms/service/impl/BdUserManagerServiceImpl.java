package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.BdUserDao;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;
import com.nuanyou.cms.service.BdUserManagerService;
import com.nuanyou.cms.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * bd宝用户管理
 *
 * Created by sharp on 2017/6/22 - 15:04
 */
@Service
public class BdUserManagerServiceImpl implements BdUserManagerService {
    
    @Autowired
    private BdUserDao bdUserDao;
    
    @Override
    public Page<BdUserManagerVo> findAllBdUsers(final BdUserManagerRequestVo requestVo) {
        // TODO: 2017/6/22 Spring data， Spring data jpa需要学习
    
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());
    
//        配置查询条件
        List<BdUser> bdUsers = bdUserDao.findAll();
    
        List<BdUserManagerVo> allCate = this.convertToBdUserManagerVo(bdUsers);
        
        Page<BdUserManagerVo> pageVOs = new PageImpl<BdUserManagerVo>(allCate, pageable, bdUsers.size());
        
        return pageVOs;
    }
    
    private List<BdUserManagerVo> convertToBdUserManagerVo(List<BdUser> entitys) {
        List<BdUserManagerVo> bdUserManagerVos = Lists.newArrayList();
        CountryService countryService = new CountryServiceImpl();
    
        //        for(BdUser user : entitys){
//            BdUserManagerVo bdUserManagerVo = BeanUtils.copyBean(user, new BdUserManagerVo());
//            bdUserManagerVos.add(bdUserManagerVo);
//        }
        for (BdUser user : entitys) {
            System.out.println("======================开始转换了======================");
            BdUserManagerVo bdUserManagerVo = new BdUserManagerVo();
            bdUserManagerVo.setId(user.getId());
            bdUserManagerVo.setName(user.getName());
            bdUserManagerVo.setCountryName(countryService.findOne(user.getCountryId()).getEname());
            bdUserManagerVo.setEmail(user.getEmail());
//            bdUserManagerVo.set
        }
        
        
        return bdUserManagerVos;
    }
}
