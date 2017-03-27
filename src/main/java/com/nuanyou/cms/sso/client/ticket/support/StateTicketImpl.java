package com.nuanyou.cms.sso.client.ticket.support;

import com.nuanyou.cms.sso.client.ticket.AbstractTicket;
import com.nuanyou.cms.sso.client.ticket.ExpirationPolicy;
import com.nuanyou.cms.sso.client.ticket.StateTicket;
import org.springframework.util.Assert;

/**
 * Created by Felix on 2017/3/22.
 */
public final class StateTicketImpl  extends AbstractTicket implements StateTicket {


    private ExpirationPolicy expirationPolicy;


    private String id;
    private long creationTime;

    private long lastTimeUsed;
    private long previousLastTimeUsed;

    private int countOfUses;



    public StateTicketImpl(final String id, final ExpirationPolicy policy) {
        super(id, policy);
        Assert.notNull(id, "ticket cannot be null");
        Assert.notNull(policy, "service cannot be null");

        this.creationTime = System.currentTimeMillis();
        this.lastTimeUsed = System.currentTimeMillis();//交给 public abstract class AbstractTicket implements Ticket, TicketState去做
        this.expirationPolicy = expirationPolicy;
    }

//    public final void updateState() {
//        this.previousLastTimeUsed = this.lastTimeUsed;
//        this.lastTimeUsed = System.currentTimeMillis();
//        this.countOfUses++;
//    }


}
