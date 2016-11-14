package com.nuanyou.cms.service;


import com.nuanyou.cms.model.PageRemote;
import com.nuanyou.cms.model.Withdraw;
import com.nuanyou.cms.model.WithdrawParam;

import java.math.BigDecimal;

/**
 * Created by Alan.ye on 2016/9/28.
 */
public interface RemoteService {

    void pushMessage(Long userId, String context, Long cmsUserId);

    void recharge(Long userid, BigDecimal amount, Integer type, Long cmsUserId, String cmsUserName) throws Exception;

    PageRemote<Withdraw> withdraw(WithdrawParam withdrawVO, Integer index);

    void operateWithdraw(String type, Integer operateid, String message);
}