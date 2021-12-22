package com.goosemagnet.oauth2.resource;

import com.goosemagnet.oauth2.service.AuthenticationService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/oauth2")
@AllArgsConstructor
public class OauthController {

    private final AuthenticationService authenticationService;

    @GET
    @Path("/github")
    public Response authWithGithub() {
        return authenticationService.redirectToLoginWithGithub();
    }

    @GET
    @Path("/google")
    public Response authWithGoogle() {
        return authenticationService.redirectToLoginWithGoogle();
    }
}
