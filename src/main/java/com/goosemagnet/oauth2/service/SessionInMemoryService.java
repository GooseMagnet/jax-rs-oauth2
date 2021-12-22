package com.goosemagnet.oauth2.service;

import com.goosemagnet.oauth2.model.User;

public class SessionInMemoryService implements SessionService{

    @Override
    public User createSessionForUser(User user) {
        return user;
    }
}
