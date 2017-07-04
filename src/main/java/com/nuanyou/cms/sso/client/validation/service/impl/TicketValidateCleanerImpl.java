package com.nuanyou.cms.sso.client.validation.service.impl;

import com.nuanyou.cms.sso.client.validation.service.TicketStateService;
import com.nuanyou.cms.sso.client.validation.vo.StateTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Felix on 2017/7/4.
 */
@Service
public class TicketValidateCleanerImpl {

    @Autowired
    private TicketStateService ticketStateService;
    
    @Scheduled(fixedRate =12*60*60*1000 )
    public void cleaner(){
        System.out.println("clean state tickets which is not availavle!");
        Collection<StateTicket> tickets = ticketStateService.getTickets();
        for (StateTicket ticket : tickets) {
            if(ticket.isExpired()){
                ticketStateService.deleteTicket(ticket.getCode());
            }
        }
    }
}
