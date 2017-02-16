package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.PushDetailDao;
import com.nuanyou.cms.dao.PushGroupDao;
import com.nuanyou.cms.entity.push.PushGroup;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.PushGroupVo;
import com.nuanyou.cms.service.PushGroupService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangkai on 2017/2/15.
 */
@Service
public class PushGroupServiceImpl implements PushGroupService {
    @Autowired
    private PushGroupDao pushGroupDao;
    @Autowired
    private PushDetailDao pushDetailDao;

    @Override
    public Page<PushGroupVo> list(int index) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        Page<PushGroup> pushGroupPage = this.pushGroupDao.findAll(pageable);

        List<PushGroupVo> pushGroupVoList = new ArrayList<>();
        for (PushGroup pushGroup : pushGroupPage.getContent()) {
            PushGroupVo pushGroupVo = BeanUtils.copyBean(pushGroup, new PushGroupVo());
            pushGroupVo.setNum(this.pushDetailDao.countByPushGroupIdAndStatusAndDeleted(pushGroup.getId(), true, false));
            pushGroupVoList.add(pushGroupVo);
        }

        return new PageImpl<>(pushGroupVoList, pageable, pushGroupPage.getTotalElements());
    }

    @Override
    public void changeStatus(Long id, Boolean status) {
        PushGroup pushGroup = this.pushGroupDao.findOne(id);
        if (pushGroup != null) {
            pushGroup.setStatus(status);
            pushGroup.setUpdateTime(new Date());
            this.pushGroupDao.save(pushGroup);
        }
    }

}
