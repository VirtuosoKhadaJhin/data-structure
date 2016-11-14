package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.MerchantStaffDao;
import com.nuanyou.cms.entity.MerchantStaff;
import com.nuanyou.cms.service.MerchantStaffService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class MerchantStaffServiceImpl implements MerchantStaffService {

    @Autowired
    private MerchantStaffDao dao;

    @Override
    public MerchantStaff saveNotNull(MerchantStaff entity) {
        String password = entity.getPassword();
        if (entity.getId() == null) {
            int count = dao.findByUsername(entity.getUsername());
            if (count > 0)
                throw new APIException(ResultCodes.UsedName);

            if (StringUtils.isBlank(password))
                password = StringUtils.leftPad(entity.getMchId().toString(), 4, '0');

            entity.setPassword(MD5Utils.encrypt(password));
            return dao.save(entity);
        } else {
            if (StringUtils.isBlank(password)) {
                entity.setPassword(null);
            } else {
                if (password.length() != 32)
                    entity.setPassword(MD5Utils.encrypt(password));
            }
            MerchantStaff oldEntity = dao.findOne(entity.getId());
            BeanUtils.copyBeanNotNull(entity, oldEntity);
            return dao.save(oldEntity);
        }
    }
}