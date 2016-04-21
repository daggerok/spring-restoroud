package com.daggerok.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
/*
    Oauth roles

    1. resource owner
        An entity capable of granting access to a protected resource.
        When the resource owner is a person, it is referred to as an end-
        user.
    2. resource server
        The server hosting the protected resources, capable of accepting
        and responding to protected resource requests using access tokens.
    3. client
        An application making protected resource requests on behalf of the
        resource owner and with its authorization.  The term client does
        not imply any particular implementation characteristics (e.g.
        whether the application executes on a server, a desktop, or other
        devices).
    4. authorization server
        The server issuing access tokens to the client after successfully
        authenticating the resource owner and obtaining authorization.

    Protocol Flow

    +--------+                               +---------------+
    |        |--(A)- Authorization Request ->|   Resource    |
    |        |                               |     Owner     |
    |        |<-(B)-- Authorization Grant ---|               |
    |        |                               +---------------+
    |        |
    |        |                               +---------------+
    |        |--(C)-- Authorization Grant -->| Authorization |
    | Client |                               |     Server    |
    |        |<-(D)----- Access Token -------|               |
    |        |                               +---------------+
    |        |
    |        |                               +---------------+
    |        |--(E)----- Access Token ------>|    Resource   |
    |        |                               |     Server    |
    |        |<-(F)--- Protected Resource ---|               |
    +--------+                               +---------------+

   The abstract OAuth 2.0 flow illustrated in Figure 1 describes the
   interaction between the four roles and includes the following steps:

   (A)  The client requests authorization from the resource owner.  The
        authorization request can be made directly to the resource owner
        (as shown), or preferably indirectly via the authorization
        server as an intermediary.
   (B)  The client receives an authorization grant, which is a
        credential representing the resource owner's authorization,
        expressed using one of four grant types defined in this
        specification or using an extension grant type.  The
        authorization grant type depends on the method used by the
        client to request authorization and the types supported by the
        authorization server.
   (C)  The client requests an access token by authenticating with the
        authorization server and presenting the authorization grant.
   (D)  The authorization server authenticates the client and validates
        the authorization grant, and if valid issues an access token.
   (E)  The client requests the protected resource from the resource
        server and authenticates by presenting the access token.
   (F)  The resource server validates the access token, and if valid,
        serves the request.

    Resource Owner Password Credentials Flow

    +----------+
    | Resource |
    |  Owner   |
    |          |
    +----------+
      v
      |    Resource Owner
     (A) Password Credentials
      |
      v
    +---------+                                  +---------------+
    |         |>--(B)---- Resource Owner ------->|               |
    |         |         Password Credentials     | Authorization |
    | Client  |                                  |     Server    |
    |         |<--(C)---- Access Token ---------<|               |
    |         |    (w/ Optional Refresh Token)   |               |
    +---------+                                  +---------------+

   (A)  The resource owner provides the client with its username and
        password.
   (B)  The client requests an access token from the authorization
        server's token endpoint by including the credentials received
        from the resource owner.  When making the request, the client
        authenticates with the authorization server.
   (C)  The authorization server authenticates the client and validates
        the resource owner credentials, and if valid issues an access
        token.

    Access Token Request

   grant_type
         REQUIRED.  Value MUST be set to "password".
   username
         REQUIRED.  The resource owner username.
   password
         REQUIRED.  The resource owner password.
   scope
         OPTIONAL.  The scope of the access request as described by

see details here: https://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-1.1
*/
@SpringCloudApplication // Order(2)
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE) // Order(1)
    protected static class AuthManager extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER").and()
                    .withUser("bob").password("bob").roles("USER").and()
                    .withUser("max").password("max").roles("USER", "ADMIN").and()
                    .withUser("admin").password("admin").roles("ADMIN");
        }
    }

    @Configuration // Order(3)
    @EnableAuthorizationServer // 1
    protected static class AuthServer extends AuthorizationServerConfigurerAdapter {

        @Autowired // 2
        private AuthenticationManager authenticationManager;

        @Override // 3
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                .inMemory()
                    .withClient("auth").secret("htua")
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes("openid,read,write,trust")
                .and()
                    .withClient("service").secret("service")
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes("openid,read,write,trust");
        }
    }

    @Configuration // Order(4)
    @EnableResourceServer
    protected static class ResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
        }
    }
}
