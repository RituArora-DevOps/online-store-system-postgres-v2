package com.oss.ossv1.session;

import com.oss.ossv1.data.entity.User;

/**
 * Represents the UserSession class.
 */
public class UserSession {
    private static UserSession instance;
    private User user;

/**
 * UserSession method.
 */
    private UserSession() {}

/**
 * getInstance method.
 */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

/**
 * setUser method.
 */
    public void setUser(User user) {
        this.user = user;
    }

/**
 * getUser method.
 */
    public User getUser() {
        return user;
    }

/**
 * isLoggedIn method.
 */
    public boolean isLoggedIn() {
        return user != null;
    }

/**
 * clear method.
 */
    public void clear() {
        user = null;
    }
}
