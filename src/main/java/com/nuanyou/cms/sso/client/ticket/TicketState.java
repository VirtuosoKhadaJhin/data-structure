/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.nuanyou.cms.sso.client.ticket;

/**
 * @author Felix
 */
public interface TicketState {

    /**
     * Returns the number of times a ticket was used.
     * 
     * @return the number of times the ticket was used.
     */
    int getCountOfUses();

    /**
     * Returns the last time the ticket was used.
     * 
     * @return the last time the ticket was used.
     */
    long getLastTimeUsed();

    /**
     * Get the second to last time used.
     * 
     * @return the previous time used.
     */

    long getPreviousTimeUsed();

    /**
     * Get the time the ticket was created.
     * 
     * @return the creation time of the ticket.
     */
    long getCreationTime();

    /**
     * Authentication information from the ticket. This may be null.
     * 
     * @return the authentication information.
     */
    //Authentication getAuthentication();
}
