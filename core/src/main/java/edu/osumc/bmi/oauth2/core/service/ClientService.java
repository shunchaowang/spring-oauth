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

  Page<ClientDetail> findAllClientDetails(Pageable pageable);

  /**
   * interface inner class has public static by default encapsulate both client and oauth client
   * detail
   */
  class ClientDetail extends OAuthClientDetail {

    private OAuthClientDetail oAuthClientDetail;
    private String name;

    public ClientDetail(OAuthClientDetail oAuthClientDetail) {
      this.oAuthClientDetail = oAuthClientDetail;
    }

    public OAuthClientDetail getoAuthClientDetail() {
      return oAuthClientDetail;
    }

    public void setoAuthClientDetail(OAuthClientDetail oAuthClientDetail) {
      this.oAuthClientDetail = oAuthClientDetail;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
