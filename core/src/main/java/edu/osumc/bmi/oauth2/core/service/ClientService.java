package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;

public interface ClientService {

  Client get(long id);

  Client findByOauth2ClientId(String clientId);

  OAuthClientDetail getOAuthClientDetail(String clientId);
}
