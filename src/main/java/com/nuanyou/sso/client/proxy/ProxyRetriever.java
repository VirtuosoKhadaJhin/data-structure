/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nuanyou.sso.client.proxy;

import java.io.Serializable;


public interface ProxyRetriever extends Serializable {

    /**
     * Retrieves a proxy ticket for a specific targetService.
     *
     * @param proxyGrantingTicketId the ProxyGrantingTicketId
     * @param targetService         the service we want to proxy.
     * @return the ProxyTicket Id if Granted, null otherwise.
     */
    String getProxyTicketIdFor(String proxyGrantingTicketId,
                               String targetService);
}
