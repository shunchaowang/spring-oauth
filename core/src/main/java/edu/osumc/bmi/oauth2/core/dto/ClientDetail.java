package edu.osumc.bmi.oauth2.core.dto;

import lombok.Value;

@Value
public class ClientDetail {
  // attributes from client class
  String name;
  Boolean active;

  // attributes from OAuth2ClientDetail class
  String clientId;
  String authorizedGrantTypes;
  String webServerRedirectUri;
  Integer accessTokenValidity;
  Integer refreshTokenValidity;
  String autoApprove;
}
