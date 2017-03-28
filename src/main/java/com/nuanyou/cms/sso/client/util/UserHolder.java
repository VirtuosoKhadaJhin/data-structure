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

package com.nuanyou.cms.sso.client.util;

import com.nuanyou.cms.sso.client.validation.User;

/**
 * Static holder that places User in a ThreadLocal.
 */
public class UserHolder {

    /**
     * ThreadLocal to hold the user for Threads to access.
     */
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<User>();


    /**
     * Retrieve the user from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static User getUser() {
        return threadLocal.get();
    }

    /**
     * Add the user to the ThreadLocal.
     *
     * @param user the user to add.
     */
    public static void setUser(final User user) {
        threadLocal.set(user);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
