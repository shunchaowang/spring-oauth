package edu.osumc.bmi.oauth2.core.data;

import lombok.Value;

@Value
public class ClientDetail {
    // attributes from client class
    private String name;
    private Boolean active;

    // attributes from OAuth2ClientDetail class
    private String clientId;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String autoApprove;
}
