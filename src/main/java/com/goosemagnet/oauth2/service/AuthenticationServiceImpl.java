package com.goosemagnet.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goosemagnet.oauth2.model.*;
import com.goosemagnet.oauth2.model.dto.OauthTokenDto;
import com.goosemagnet.oauth2.model.dto.UserCredentialsDto;
import com.goosemagnet.oauth2.util.JacksonMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.goosemagnet.oauth2.util.HttpConstants.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private static final ObjectMapper MAPPER = JacksonMapper.createMapper();

    private final OkHttpClient httpClient;
    private final Map<OauthProvider, OauthProviderDetails> oAuthProviderDetails;

    public AuthenticationServiceImpl(OkHttpClient httpClient, Map<OauthProvider, OauthProviderDetails> oAuthProviderDetails) {
        this.httpClient = httpClient;
        this.oAuthProviderDetails = oAuthProviderDetails;
    }

    @Override
    public Optional<User> getUserFromCredentials(UserCredentialsDto credentials) {
        return Optional.empty();
    }

    @SneakyThrows
    @Override
    public Optional<User> getUserFromAuthCode(OauthProvider oauthProvider, String code) {
        OauthTokenDto oauthTokenDto = getOauthTokenForProvider(oauthProvider, code);
        OauthUserDetails oauthUserDetails = getOauthUserDetails(oauthProvider, oauthTokenDto);
        return Optional.of(mapOauthUserToUser(oauthUserDetails));
    }

    @Override
    public Response redirectToLoginWithGithub() {
        return createRedirectResponse(OauthProvider.GITHUB);
    }

    @Override
    public Response redirectToLoginWithGoogle() {
        return createRedirectResponse(OauthProvider.GOOGLE);
    }

    @SneakyThrows
    private OauthUserDetails getOauthUserDetails(OauthProvider oauthProvider, OauthTokenDto tokenDto) {
        val request = createUserRequestFromToken(oauthProvider, tokenDto);
        val response = httpClient.newCall(request).execute();
        val userDetailsJson = Objects.requireNonNull(response.body()).string();
        return MAPPER.readValue(userDetailsJson, OauthUserDetails.class);
    }

    @SneakyThrows
    private OauthTokenDto getOauthTokenForProvider(OauthProvider oauthProvider, String code) {
        val authenticationRequestBody = createAuthenticationRequestBody(oauthProvider, code);
        val request = createAuthenticationRequest(oauthProvider, authenticationRequestBody);
        val response = httpClient.newCall(request).execute();
        val accessTokenJson = Objects.requireNonNull(response.body()).string();
        return MAPPER.readValue(accessTokenJson, OauthTokenDto.class);
    }

    private URI createRedirectUriForProvider(OauthProvider oAuthProvider) {
        OauthProviderDetails providerDetails = oAuthProviderDetails.get(oAuthProvider);
        return UriBuilder.fromPath(providerDetails.getAuthUrl())
                .queryParam(RESPONSE_TYPE, CODE)
                .queryParam(CLIENT_ID, providerDetails.getClientId())
                .queryParam(SCOPE, providerDetails.getScopes())
                .queryParam(REDIRECT_URI, providerDetails.getRedirectUrl())
                .build();
    }

    private Response createRedirectResponse(OauthProvider oAuthProvider) {
        return Response.status(Response.Status.FOUND)
                .location(createRedirectUriForProvider(oAuthProvider))
                .build();
    }

    private Request createUserRequestFromToken(OauthProvider provider, OauthTokenDto tokenDto) {
        OauthProviderDetails providerDetails = oAuthProviderDetails.get(provider);
        return new Request.Builder()
                .url(providerDetails.getUserUrl())
                .addHeader(AUTHORIZATION, String.format("%s %s", BEARER, tokenDto.getAccessToken()))
                .build();
    }

    private RequestBody createAuthenticationRequestBody(OauthProvider oAuthProvider, String code) {
        OauthProviderDetails provider = oAuthProviderDetails.get(oAuthProvider);
        return new FormBody.Builder()
                .add(CLIENT_ID, provider.getClientId())
                .add(CLIENT_SECRET, provider.getClientSecret())
                .add(CODE, code)
                .add(REDIRECT_URI, provider.getRedirectUrl())
                .add(GRANT_TYPE, AUTHORIZATION_CODE)
                .build();
    }

    private Request createAuthenticationRequest(OauthProvider oauthProvider, RequestBody requestBody) {
        OauthProviderDetails providerDetails = oAuthProviderDetails.get(oauthProvider);
        return new okhttp3.Request.Builder()
                .url(providerDetails.getTokenUrl())
                .post(requestBody)
                .addHeader(ACCEPT, MediaType.APPLICATION_JSON)
                .build();
    }

    private User mapOauthUserToUser(OauthUserDetails oAuthUser) {
        val name = Objects.isNull(oAuthUser.getName()) ? oAuthUser.getLogin() : oAuthUser.getName();
        return new User(name, UserType.OAUTH);
    }
}
