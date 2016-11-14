package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.FakeOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Felix on 2016/10/27.
 */
public interface FakeOrderService {

    public FakeOrder saveNotNull(FakeOrder entity);

    Page<FakeOrder> findByCondition(FakeOrder fakeOrder, Pageable pageable);
}
