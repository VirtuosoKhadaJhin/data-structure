/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.nuanyou.sso.client.ticket;

import java.io.Serializable;

/**
 * Interface for the generic concept of a ticket.
 */
public interface Ticket extends Serializable {

    /**
     * Method to retrieve the id.
     * 
     * @return the id
     */
    String getId();

    /**
     * Determines if the ticket is expired. Most common implementations might
     * collaborate with <i>ExpirationPolicy </i> strategy.
     *
     */
    boolean isExpired();


    /**
     * Method to return the time the Ticket was created.
     * 
     * @return the time the ticket was created.
     */
    long getCreationTime();
    
    /**
     * Returns the number of times this ticket was used.
     * @return
     */
    int getCountOfUses();
}
