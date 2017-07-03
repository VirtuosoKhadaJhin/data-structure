

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
