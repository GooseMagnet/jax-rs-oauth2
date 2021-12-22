package com.goosemagnet.oauth2.service;

import com.goosemagnet.oauth2.model.User;

public interface SessionService {
    User createSessionForUser(User user);
}
