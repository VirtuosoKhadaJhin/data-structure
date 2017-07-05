package com.nuanyou.cms.sso.client.validation.service.impl;

import com.nuanyou.cms.sso.client.validation.service.TicketStateService;
import com.nuanyou.cms.sso.client.validation.vo.StateTicket;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Felix on 2017/7/3.
 */
@Service
public  class StateTicketServiceImpl implements TicketStateService{

    private final Map<String, StateTicket> cache=new ConcurrentHashMap<String, StateTicket>();;

    @Override
    public void addTicket(StateTicket ticket) {
        Assert.notNull(ticket, "ticket cannot be null");
            cache.put(ticket.getCode(),ticket);
    }

    @Override
    public StateTicket getTicket(String code) {
        if (code == null) {
            return null;
        }
        StateTicket state = this.cache.get(code);
        return state;
    }

    @Override
    public boolean deleteTicket(String code) {
        if (code == null) {
            return false;
        }
            return (this.cache.remove(code) != null);

    }

    @Override
    public Collection<StateTicket> getTickets() {
        return Collections.unmodifiableCollection(this.cache.values());
    }

}
