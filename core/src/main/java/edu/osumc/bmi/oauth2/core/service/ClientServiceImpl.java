package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  @Autowired private ClientRepository clientRepository;
  @Autowired private OAuthClientDetailRepository oAuthClientDetailRepository;

  @Override
  public Client get(long id) throws EntityNotFoundException {
    return clientRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User " + id + " not exists."));
  }

  @Override
  public Optional<Client> findByOAuth2ClientId(String clientId) {
    return clientRepository.findByOauth2ClientId(clientId);
  }

  @Override
  public Optional<OAuthClientDetail> getOAuthClientDetail(String clientId) {
    return oAuthClientDetailRepository.findByClientId(clientId);
  }

  @Override
  public Page<OAuthClientDetail> findAllOAuthClientDetails(Pageable pageable) {
    return oAuthClientDetailRepository.findAll(pageable);
  }
}
