/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.nuanyou.sso.client.ticket.support;

import com.nuanyou.sso.client.ticket.ExpirationPolicy;
import com.nuanyou.sso.client.ticket.TicketState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ExpirationPolicy that is based on certain number of uses of a ticket or a
 * certain time period for a ticket to exist.
 */
@Component
public final class MultiTimeUseOrTimeoutExpirationPolicy implements
    ExpirationPolicy {

    /** Serializable Unique ID. */
    private static final long serialVersionUID = 3257844372614558261L;

    /** The time to kill in millseconds. */

    //private final long timeToKillInMilliSeconds=100000;

    /** The maximum number of uses before expiration. */
    private final int numberOfUses=1;



    @Value("${sso.stateExpiredInMilliSeconds}")
    private   Long stateExpiredInMilliSeconds;

    public MultiTimeUseOrTimeoutExpirationPolicy() {
        //this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
        //this.numberOfUses = numberOfUses;
        //Assert.isTrue(this.numberOfUses > 0, "numberOfUsers must be greater than 0.");
        //Assert.isTrue(this.stateExpiredInMilliSeconds > 0, "timeToKillInMilliseconds must be greater than 0.");

    }

    public boolean isExpired(final TicketState ticketState) {
        Long interval=System.currentTimeMillis() - ticketState.getLastTimeUsed();
        //|| (ticketState.getCountOfUses() >= this.numberOfUses)
        return (ticketState == null)|| (interval >= this.stateExpiredInMilliSeconds);
    }


}
