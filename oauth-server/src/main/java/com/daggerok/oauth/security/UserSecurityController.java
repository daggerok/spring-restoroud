package com.daggerok.oauth.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/*
$ curl auth:htua@localhost:8000/uaa/oauth/token \
    -d 'grant_type=password' \
    -d username=max \
    -d password=max
{
    "access_token":"f14bd298-3a6d-459d-971e-400e5741c4ef",
    "token_type":"bearer",
    "refresh_token":"f5002f6a-3cda-4fa1-9d01-318d5e546204",
    "expires_in":43199,"scope":"openid"
}

This project is a simple, minimal implementation of an OAuth2 Authorization Server for use with Spring Cloud sample apps
It has a context root of /uaa (so that it won't share cookies with other apps running on other ports on the root resource)

OAuth2 endpoints are:

/uaa/oauth/token
    the Token endpoint, for clients to acquire access tokens. There is one client ("auth" with secret "htua")
    With Spring Cloud Security this is the oauth2.client.tokenUri

/uaa/oauth/authorize
    the Authorization endpoint to obtain user approval for a token grant
    Spring Cloud Security configures this in a client app as oauth2.client.authorizationUri

/uaa/oauth/check_token
    the Check Token endpoint (not part of the OAuth2 spec)
    Can be used to decode a token remotely
    Spring Cloud Security configures this in a client app as oauth2.resource.tokenInfoUri
 */
@RestController
public class UserSecurityController {

    @RequestMapping({ "/user", "/me" })
    public Map<String, Object> user(Principal user) {
        Map<String, Object> map = new LinkedHashMap<>();
        OAuth2Authentication authentication = (OAuth2Authentication) user;
        Optional.ofNullable(authentication).ifPresent(oAuth2Authentication ->
            map.put("oauth.principal", oAuth2Authentication.getUserAuthentication()));

        map.put("user", user);
        return map;
    }
}
