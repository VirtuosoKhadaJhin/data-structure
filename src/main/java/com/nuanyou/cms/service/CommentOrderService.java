package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;

/**
 * Created by Alan.ye on 2016/9/26.
 */
public interface CommentOrderService {

    Page<CommentOrder> findByCondition(Integer index, CommentOrder entity, TimeCondition time, String scoreStr);

    CommentOrder saveNotNull(CommentOrder entity);

}