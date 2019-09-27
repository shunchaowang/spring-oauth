package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public interface ClientService {

  Client get(long id) throws EntityNotFoundException;

  Optional<Client> findByOauth2ClientId(String oauth2ClientId) throws EntityNotFoundException;

  Optional<OAuthClientDetail> getOAuthClientDetail(String oauth2ClientId)
      throws EntityNotFoundException;

  Page<OAuthClientDetail> findAllOAuthClientDetails(Pageable pageable);

  Optional<ClientDetail> findClientDetail(String oauth2ClientId);

  Page<ClientDetail> findAllClientDetails(Pageable pageable);

  /**
   * interface inner class has public static by default encapsulate both client and oauth client
   * detail. this class should be immutable
   */
  class ClientDetail {

    // attributes from client class
    private String name;
    private boolean active;

    // attributes from OAuth2ClientDetail class
    private String clientId;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private int accessTokenValidity;
    private int refreshTokenValidity;
    private String autoApprove;

    public ClientDetail(OAuthClientDetail oAuthClientDetail, Client client) {
      this(oAuthClientDetail);
      name = client.getName();
      active = client.getActive();
    }

    public ClientDetail(OAuthClientDetail oAuthClientDetail) {

      clientId = oAuthClientDetail.getClientId();
      authorizedGrantTypes = oAuthClientDetail.getAuthorizedGrantTypes();
      webServerRedirectUri = oAuthClientDetail.getWebServerRedirectUri();
      accessTokenValidity = oAuthClientDetail.getAccessTokenValidity();
      refreshTokenValidity = oAuthClientDetail.getRefreshTokenValidity();
      autoApprove = oAuthClientDetail.getAutoApprove();
    }

    public String getName() {
      return name;
    }

    public boolean isActive() {
      return active;
    }

    public String getClientId() {
      return clientId;
    }

    public String getAuthorizedGrantTypes() {
      return authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
      return webServerRedirectUri;
    }

    public int getAccessTokenValidity() {
      return accessTokenValidity;
    }

    public int getRefreshTokenValidity() {
      return refreshTokenValidity;
    }

    public String getAutoApprove() {
      return autoApprove;
    }
  }
}
