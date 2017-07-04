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

package com.nuanyou.cms.sso.client.validation.exception;

/**
 * ticket验证失效
 */
public class TicketValidationException extends Exception {


    public TicketValidationException(final String string) {
        super(string);
    }


    public TicketValidationException(final String string, final Throwable throwable) {
        super(string, throwable);
    }


    public TicketValidationException(final Throwable throwable) {
        super(throwable);
    }
}
