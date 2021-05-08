package edu.osumc.bmi.oauth2.auth.client;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

@Component
public class AuthClientDetailsService implements ClientDetailsService {

  @Autowired private ClientService clientService;

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
    OAuthClientDetail clientDetail =
        clientService
            .getOAuthClientDetail(clientId)
            .orElseThrow(
                () -> new ClientRegistrationException("Client " + clientId + " not exists."));
    return new AuthClientDetails(clientDetail);
  }
}
