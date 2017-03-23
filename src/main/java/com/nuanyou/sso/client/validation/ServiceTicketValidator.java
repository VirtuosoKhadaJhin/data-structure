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

package com.nuanyou.sso.client.validation;


import com.alibaba.fastjson.JSON;
import com.nuanyou.sso.client.util.XmlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Implementation of the TicketValidator that will validate Service Tickets in compliance with the CAS 2.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class ServiceTicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {


    /**
     * Constructs an instance of the CAS 2.0 Service Ticket Validator with the supplied
     * CAS server url prefix.
     *
     * @param casServerUrlPrefix the CAS Server URL prefix.
     */
    public ServiceTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }


    protected String getUrlSuffix() {
        return "serviceValidate";
    }

    protected final User parseResponseFromServer(final String response) throws TicketValidationException {
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

    /**
     * Default attribute parsing of attributes that look like the following:
     * &lt;cas:attributes&gt;
     *  &lt;cas:attribute1&gt;value&lt;/cas:attribute1&gt;
     *  &lt;cas:attribute2&gt;value&lt;/cas:attribute2&gt;
     * &lt;/cas:attributes&gt;
     * <p>
     * This code is here merely for sample/demonstration purposes for those wishing to modify the CAS2 protocol.  You'll
     * probably want a more robust implementation or to use SAML 1.1
     *
     * @param xml the XML to parse.
     * @return the map of attributes.
     */
    protected Map<String,Object> extractCustomAttributes(final String xml) {
    	final int pos1 = xml.indexOf("<cas:attributes>");
    	final int pos2 = xml.indexOf("</cas:attributes>");
    	
    	if (pos1 == -1) {
    		return Collections.emptyMap();
    	}
    	
    	final String attributesText = xml.substring(pos1+16, pos2);
    	
    	final Map<String,Object> attributes = new HashMap<String,Object>();
    	final BufferedReader br = new BufferedReader(new StringReader(attributesText));
    	
    	String line;
    	final List<String> attributeNames = new ArrayList<String>();
    	try {
	    	while ((line = br.readLine()) != null) {
	    		final String trimmedLine = line.trim();
	    		if (trimmedLine.length() > 0) {
		    		final int leftPos = trimmedLine.indexOf(":");
		    		final int rightPos = trimmedLine.indexOf(">");
		    		attributeNames.add(trimmedLine.substring(leftPos+1, rightPos));
	    		}
	    	}
	    	br.close();
    	} catch (final IOException e) {
    		//ignore
    	}

        for (final String name : attributeNames) {
            final List<String> values = XmlUtils.getTextForElements(xml, name);

            if (values.size() == 1) {
                attributes.put(name, values.get(0));
            } else {
                attributes.put(name, values);
            }
    	}
    	
    	return attributes;
    }


}
