package edu.osumc.bmi.oauth2.core.dto;

import lombok.Value;

@Value
public class ClientDetail {
  // attributes from client class
  String name;
  boolean active;

  // attributes from OAuth2ClientDetail class
  String clientId;
  String authorizedGrantTypes;
  String webServerRedirectUri;
  int accessTokenValidity;
  int refreshTokenValidity;
  String autoApprove;
}
