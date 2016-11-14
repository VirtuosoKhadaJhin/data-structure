package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.FakeUserDao;
import com.nuanyou.cms.entity.FakeUser;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.FakeUserService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class FakeUserServiceImpl implements FakeUserService {

    @Autowired
    private FakeUserDao fakeUserDao;

    @Override
    public Page<FakeUser> findByCondition(Integer index, FakeUser entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("nickname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        return fakeUserDao.findAll(Example.of(entity, matcher), pageable);
    }

    @Override
    public FakeUser saveNotNull(FakeUser entity) {
        if (entity.getId() == null) {
            return fakeUserDao.save(entity);
        }
        FakeUser oldEntity = fakeUserDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return fakeUserDao.save(oldEntity);
    }

    @Override
    public List<FakeUser> getIdNameList() {
        return fakeUserDao.getIdNameList();
    }
}
