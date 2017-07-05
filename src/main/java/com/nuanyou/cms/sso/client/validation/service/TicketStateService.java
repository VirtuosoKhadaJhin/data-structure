package com.nuanyou.cms.sso.client.validation.service;


import com.nuanyou.cms.sso.client.validation.vo.StateTicket;

import java.util.Collection;

/**
 * Created by Felix on 2017/7/3.
 */
public interface TicketStateService {

    void addTicket(StateTicket ticket);

    StateTicket getTicket(String code);

    boolean deleteTicket(String code);

    Collection<StateTicket> getTickets();

}
