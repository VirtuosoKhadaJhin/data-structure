package com.nuanyou.cms.sso.client.validation.service;


import com.nuanyou.cms.sso.client.validation.vo.StateTicket;

import java.util.Collection;

/**
 * Created by Felix on 2017/7/3.
 */
public interface TicketStateService {
    public void addTicket(StateTicket ticket);
    public StateTicket getTicket(String code);
    public boolean deleteTicket(String code);
    public Collection<StateTicket> getTickets();


}
