package com.goosemagnet.oauth2.service;

import com.goosemagnet.oauth2.model.OauthProvider;
import com.goosemagnet.oauth2.model.User;
import com.goosemagnet.oauth2.model.dto.UserCredentialsDto;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

public interface AuthenticationService {
    Optional<User> getUserFromCredentials(UserCredentialsDto credentials);
    Optional<User> getUserFromAuthCode(OauthProvider provider, String code);
    Response redirectToLoginWithGithub();
    Response redirectToLoginWithGoogle();
}
