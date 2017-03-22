/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.nuanyou.cms.sso.client.ticket.support;

import com.nuanyou.cms.sso.client.ticket.ExpirationPolicy;
import com.nuanyou.cms.sso.client.ticket.TicketState;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * ExpirationPolicy that is based on certain number of uses of a ticket or a
 * certain time period for a ticket to exist.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
@Service
public final class MultiTimeUseOrTimeoutExpirationPolicy implements
    ExpirationPolicy {

    /** Serializable Unique ID. */
    private static final long serialVersionUID = 3257844372614558261L;

    /** The time to kill in millseconds. */
    private final long timeToKillInMilliSeconds=100000;

    /** The maximum number of uses before expiration. */
    private final int numberOfUses=1;

    public MultiTimeUseOrTimeoutExpirationPolicy() {
        //this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
        //this.numberOfUses = numberOfUses;
        Assert.isTrue(this.numberOfUses > 0, "numberOfUsers must be greater than 0.");
        Assert.isTrue(this.timeToKillInMilliSeconds > 0, "timeToKillInMilliseconds must be greater than 0.");

    }

    public boolean isExpired(final TicketState ticketState) {
        return (ticketState == null)
            || (ticketState.getCountOfUses() >= this.numberOfUses)
            || (System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds);
    }


}
