package com.goosemagnet.oauth2.resource;

import com.goosemagnet.oauth2.exception.UnauthorizedException;
import com.goosemagnet.oauth2.model.OauthProvider;
import com.goosemagnet.oauth2.model.User;
import com.goosemagnet.oauth2.model.dto.UserCredentialsDto;
import com.goosemagnet.oauth2.service.AuthenticationService;
import com.goosemagnet.oauth2.service.SessionService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Optional;

import static com.goosemagnet.oauth2.util.HttpConstants.*;

@Path("/sessions")
public class SessionController {

    public static final String ORIGIN = "http://localhost:3000";

    @Context
    private UriInfo uriInfo;

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;


    public SessionController(AuthenticationService authenticationService, SessionService sessionService) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @POST
    @Path("/credentials")
    public User loginWithCredentials(UserCredentialsDto userCredentialsDto) {
        return authenticationService.getUserFromCredentials(userCredentialsDto)
                .map(sessionService::createSessionForUser)
                .orElseThrow(UnauthorizedException::new);
    }

    @GET
    @Path("/github")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSessionForGithubAuth() {
        return Response.status(Response.Status.FOUND)
                .entity(createSessionForProvider(OauthProvider.GITHUB))
                .build();
    }

    @GET
    @Path("/google")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSessionForGoogleAuth() {
        return Response.status(Response.Status.FOUND)
                .location(URI.create(ORIGIN))
                .entity(createSessionForProvider(OauthProvider.GOOGLE))
                .build();
    }

    private User createSessionForProvider(OauthProvider provider) {
        return getCodeParam()
                .flatMap(code -> authenticationService.getUserFromAuthCode(provider, code))
                .map(sessionService::createSessionForUser)
                .orElseThrow(UnauthorizedException::new);
    }

    private Optional<String> getCodeParam() {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        return Optional.ofNullable(queryParameters.getFirst(CODE));
    }
}
