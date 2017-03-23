package com.nuanyou.sso.client.ticket;

import com.nuanyou.sso.client.ticket.support.StateTicketImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by Felix on 2017/3/22.
 */
@Service
public class GrantTicketService extends AbstractTicket{

    private final HashMap<String,String> services = new HashMap<String, String>();
    public  StateTicket grantStateTicket(String state, ExpirationPolicy expirationPolicy,String url) {
        final StateTicketImpl serviceTicket = new StateTicketImpl(state,expirationPolicy);
        serviceTicket.updateState();
        this.services.put(state, url);
        return serviceTicket;
    }
}
