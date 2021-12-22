package com.goosemagnet.oauth2.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class OauthUserDetails {
    String id;
    @JsonAlias("avatar_url")
    String avatarUrl;
    String login;
    String name;
    String email;
}
