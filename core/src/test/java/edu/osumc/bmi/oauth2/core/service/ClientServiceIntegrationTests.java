package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
public class ClientServiceIntegrationTests {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private ClientService clientService;
  @MockBean private ClientRepository clientRepository;
  @MockBean private OAuthClientDetailRepository oAuthClientDetailRepository;
  private Pageable firstPageWithTwoElementsSortByNameDesc =
      PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "name"));

  @Before
  public void setUp() {
    int count = 2;
    String clientId = "client_id";

    // mock oauthClientDetails Page
    List<OAuthClientDetail> oAuthClientDetailList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      //            String name = RandomStringUtils.random(8);
      OAuthClientDetail oAuthClientDetail = new OAuthClientDetail();
      oAuthClientDetail.setClientId(clientId + " " + i);
      oAuthClientDetailList.add(oAuthClientDetail);
    }

    Page<OAuthClientDetail> oAuthClientDetails =
        new PageImpl<>(oAuthClientDetailList, firstPageWithTwoElementsSortByNameDesc, 10);

    Mockito.when(oAuthClientDetailRepository.findAll(firstPageWithTwoElementsSortByNameDesc))
        .thenReturn(oAuthClientDetails);

    // mock client by name
    for (int i = 0; i < count; i++) {
      Client client = new Client();
      client.setName(new RandomStringGenerator.Builder().withinRange('a', 'z').build().generate(8));
      client.setOauth2ClientId(clientId + " " + i);
      Mockito.when(clientRepository.findByOauth2ClientId(clientId + " " + i))
          .thenReturn(Optional.of(client));
    }
  }

  @Test
  public void whenFindAll_thenReturnPageableClientDetails() {

    // given
    // given by setup

    // when
    Page<ClientService.ClientDetail> clientDetails =
        clientService.findAllClientDetails(firstPageWithTwoElementsSortByNameDesc);
    // then
    assertThat(clientDetails.getTotalElements()).isEqualTo(10);
    logger.debug("total elements - {}", clientDetails.getTotalElements());
    assertThat(clientDetails.getNumberOfElements()).isEqualTo(2);
    logger.debug("number of elements - {}", clientDetails.getNumberOfElements());
    logger.debug("number - {}", clientDetails.getNumber());
    logger.debug("size - {}", clientDetails.getSize());
    logger.debug("content list size - {}", clientDetails.getContent().size());
    clientDetails.forEach(
        (clientDetail) -> {
            logger.debug("client detail is named - {}", clientDetail.getName());
            logger.debug("client id - {}", clientDetail.getOAuthClientDetail().getClientId());
        });
  }

  @TestConfiguration
  static class ClientServiceTestsContextConfiguration {

    @Bean
    public ClientService clientService() {
      return new ClientServiceImpl();
    }
  }
}
