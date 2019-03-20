package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  @Autowired private ClientRepository clientRepository;

  @Override
  public Client get(long id) {
    return clientRepository.getOne(id);
  }

  @Override
  public Client findByOauth2ClientId(String clientId) {
    return clientRepository.findByOauth2ClientId(clientId);
  }
}
