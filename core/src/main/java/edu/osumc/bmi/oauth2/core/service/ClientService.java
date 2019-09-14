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
}
