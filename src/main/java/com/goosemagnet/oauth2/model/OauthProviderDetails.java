package com.goosemagnet.oauth2.model;

import lombok.Value;

@Value
public class OauthProviderDetails {
    String authUrl;
    String tokenUrl;
    String userUrl;
    String clientId;
    String clientSecret;
    String redirectUrl;
    String scopes;
}
