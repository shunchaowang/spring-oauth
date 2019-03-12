package edu.osumc.bmi.oauth2.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

  @Autowired
  private ClientRepository clientRepository;

  @Override
  public Client get(long id) {
    return clientRepository.getOne(id);
  }

  @Override
  public Client findByOauth2ClientId(String clientId) {
    return clientRepository.findByOauth2ClientId(clientId);
  }
}
