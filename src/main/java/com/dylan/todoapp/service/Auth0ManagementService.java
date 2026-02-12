package com.dylan.todoapp.service;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Auth0ManagementService {

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.auth0.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuer;

    private String getApiToken(String domain) throws Auth0Exception {
        AuthAPI auth = AuthAPI.newBuilder(domain, clientId, clientSecret).build();

        TokenRequest request = auth.requestToken("https://" + domain + "/api/v2/");
        TokenHolder holder = request.execute().getBody();
        return holder.getAccessToken();
    }

}