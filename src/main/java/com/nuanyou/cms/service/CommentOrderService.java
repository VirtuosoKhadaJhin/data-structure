package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.CommentReply;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/26.
 */
public interface CommentOrderService {

    Page<CommentOrder> findByCondition(CommentOrder entity, TimeCondition time, String scoreStr, Pageable pageable);

    CommentOrder saveNotNull(CommentOrder entity);

    void delete(Long id);

    void reply(CommentReply entity);

    List<CommentReply> findReply(Long id);

    void showOrHideComment(Long id, Boolean isShow);

    List<CommentOrder> findByCondition(CommentOrder entity, TimeCondition time,String scoreStr);
}