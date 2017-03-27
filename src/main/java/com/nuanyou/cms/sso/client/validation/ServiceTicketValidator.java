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

package com.nuanyou.cms.sso.client.validation;


import com.alibaba.fastjson.JSON;

/**
 * Implementation of the TicketValidator that will validate Service Tickets in compliance with the SSO Server.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class ServiceTicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {


    /**
     * Constructs an instance of the SSO  Service Ticket Validator with the supplied
     * SSO server url prefix.
     *
     * @param validateCodeUrl the SSO Server URL prefix.
     */
    public ServiceTicketValidator(final String validateCodeUrl) {
        super(validateCodeUrl);
    }


    protected String getUrlSuffix() {
        return "serviceValidate";
    }

    protected final User parseResponseFromServer(final String response) throws TicketValidationException {
        System.out.println("Successfully retrive user info: "+response);
        Integer code= JSON.parseObject(response).getInteger("code");
        User user=null;
        if(code!=null&&code==0){
            user= JSON.parseObject(JSON.parseObject(response).get("data").toString(),User.class);
            if(user!=null){

            }else{
                throw new TicketValidationException("No user was found in the response from the SSO server.");
            }
        }else{
            throw new TicketValidationException("No user was found in the response from the SSO server.");
        }
        return user;

    }




}
