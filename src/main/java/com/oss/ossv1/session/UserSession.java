package com.oss.ossv1.session;

import com.oss.ossv1.data.entity.User;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void clear() {
        user = null;
    }
}
