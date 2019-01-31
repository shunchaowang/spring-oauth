package edu.osumc.bmi.oauth2.auth.client;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

// @Component("clientDetailsService")
public class ClientDetailsServiceImpl implements ClientDetailsService {
  /**
   * Load a client by the client id. This method must not return null.
   *
   * @param clientId The client id.
   * @return The client details (never null).
   * @throws ClientRegistrationException If the client account is locked, expired, disabled, or
   *     invalid for any other reason.
   */
  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    return null;
  }
}
