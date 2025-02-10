package com.eaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
public class ClientController {

    @Autowired
    private OAuth2AuthorizedClientService auth2AuthorizedClientService;

    @GetMapping("/token")
    public String getToken(Principal principal) {
        String accessToken = auth2AuthorizedClientService
                .loadAuthorizedClient("reg-client", principal.getName())
                .getAccessToken().getTokenValue();
        return accessToken;
    }

    @GetMapping("/message")
    public String message(Principal principal) {
        System.out.println("principal.getName() -->" + principal.getName());

        var restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        String accessToken = auth2AuthorizedClientService
                .loadAuthorizedClient("reg-client", principal.getName())
                .getAccessToken().getTokenValue();
        System.out.println("access Token = " + accessToken);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8181/hello", HttpMethod.GET, entity, String.class);
        return "Success :: " + response.getBody();
    }
}
