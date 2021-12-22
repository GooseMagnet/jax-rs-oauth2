package com.goosemagnet.oauth2.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class OauthTokenDto {
    @JsonAlias("access_token")
    String accessToken;
    @JsonProperty("token_type")
    String tokenType;
    String scope;
}
