package com.goosemagnet.oauth2.util;

import com.goosemagnet.oauth2.model.OauthProvider;
import com.goosemagnet.oauth2.model.OauthProviderDetails;
import com.goosemagnet.oauth2.resource.OauthController;
import com.goosemagnet.oauth2.resource.SessionController;
import com.goosemagnet.oauth2.service.AuthenticationServiceImpl;
import com.goosemagnet.oauth2.service.SessionInMemoryService;
import jakarta.ws.rs.ApplicationPath;
import okhttp3.OkHttpClient;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.Map;

@ApplicationPath("/")
public class ApplicationConfiguration extends ResourceConfig {

    public ApplicationConfiguration() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        OauthProviderDetails githubDetails = new OauthProviderDetails(
                "https://github.com/login/oauth/authorize",
                "https://github.com/login/oauth/access_token",
                "https://api.github.com/user",
                "88ce0c287c50eb8877d9",
                "3c23e132aa6b64c63df78204e6610a3a66f39c0b",
                "http://localhost:8080/sessions/github",
                "user:email"
        );
        OauthProviderDetails googleDetails = new OauthProviderDetails(
                "https://accounts.google.com/o/oauth2/v2/auth",
                "https://oauth2.googleapis.com/token",
                "https://www.googleapis.com/oauth2/v3/userinfo",
                "349180871544-0rekjfl6fohegilb8t13o17l3oqchjr5.apps.googleusercontent.com",
                "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",
                "http://localhost:8080/sessions/google",
                "profile email"
        );
        Map<OauthProvider, OauthProviderDetails> providerMap = Map.of(
                OauthProvider.GITHUB, githubDetails,
                OauthProvider.GOOGLE, googleDetails
        );
        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(httpClient, providerMap);

        register(JacksonMapper.class);
        register(new SessionController(authenticationService, new SessionInMemoryService()));
        register(new OauthController(authenticationService));
    }
}
