package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CmsUserDao;
import com.nuanyou.cms.entity.CmsUser;
import com.nuanyou.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2017/5/31.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private CmsUserDao cmsUserDao;

    @Override
    public CmsUser getUserByEmail(String email) {
        return cmsUserDao.findByEmail(email);
    }

    @Override
    public String findNameById(Long id) {
        if(id==null){
            return null;
        }
        CmsUser user = cmsUserDao.findOne(id);
        if(user==null){
            throw new APIException(ResultCodes.Fail,"查询用户失败,id"+id);
        }
        return user.getUsername();
    }
}
