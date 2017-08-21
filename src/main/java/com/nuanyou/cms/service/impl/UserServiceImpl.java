package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CmsUserDao;
import com.nuanyou.cms.entity.CmsUser;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
        return user.getName();
    }

    @Override
    public List<Long> findUserCountryId() {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ra.getRequest();
        HttpSession session = request.getSession();
//        if (session.getAttribute("countryIds") != null )
//            return (List)session.getAttribute("countryIds");
        String email = UserHolder.getUser().getEmail();
        CmsUser user = cmsUserDao.findByEmail(email);
        if(user==null){
            throw new APIException(ResultCodes.Fail,"查询用户失败");
        }
        String uri = request.getRequestURI();
        List<Object[]> list = cmsUserDao.findCountryIdByUserMenu(user.getId(),uri);
        List<Long> countryIds = new ArrayList<>();
        for (Object[] objects : list) {
            countryIds.add(Long.parseLong(objects[0].toString()));
        }
//        session.setAttribute("countryIds",countryIds);
        return countryIds;
    }

}
