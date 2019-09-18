package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
  public Optional<Client> findByOauth2ClientId(String clientId) {
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

  @Override
  public Page<ClientDetail> findAllClientDetails(Pageable pageable) {

    Page<OAuthClientDetail> oAuthClientDetails = oAuthClientDetailRepository.findAll(pageable);
    List<ClientDetail> clientDetailList = new ArrayList<>();
    oAuthClientDetails.forEach(
        (oAuthClientDetail -> {
          ClientDetail clientDetail = new ClientDetail(oAuthClientDetail);
          Optional<Client> clientOptional =
              clientRepository.findByOauth2ClientId(oAuthClientDetail.getClientId());
          clientOptional.ifPresent(client -> clientDetail.setName(client.getName()));
          clientDetailList.add(clientDetail);
        }));
    Page<ClientDetail> clientDetails =
        new PageImpl<>(
            clientDetailList,
            oAuthClientDetails.getPageable(),
            oAuthClientDetails.getTotalElements());

    return clientDetails;
  }
}
