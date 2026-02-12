package com.dylan.todoapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

@Controller
@RequestMapping("/debug")
public class DebugController {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public DebugController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/tokens")
    public String displayTokens(@AuthenticationPrincipal OidcUser oidcUser,
                                OAuth2AuthenticationToken authentication,
                                Model model) throws Exception {

        ObjectMapper mapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();

        String idTokenJson = mapper.writeValueAsString(oidcUser.getIdToken().getClaims());
        model.addAttribute("idTokenRaw", oidcUser.getIdToken().getTokenValue());
        model.addAttribute("idTokenJson", idTokenJson);

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessTokenRaw = client.getAccessToken().getTokenValue();
        model.addAttribute("accessTokenRaw", accessTokenRaw);

        try {
            String[] chunks = accessTokenRaw.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(chunks[1]));
            Object json = mapper.readValue(payload, Object.class);
            model.addAttribute("accessTokenJson", mapper.writeValueAsString(json));
        } catch (Exception e) {
            model.addAttribute("accessTokenJson", "Not a JWT (Opaque Token)");
        }

        return "debug-tokens";
    }

}
