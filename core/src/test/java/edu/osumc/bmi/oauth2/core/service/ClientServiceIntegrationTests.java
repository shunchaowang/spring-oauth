package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class ClientServiceIntegrationTests {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private ClientService clientService;
  @MockBean private ClientRepository clientRepository;
  @MockBean private OAuthClientDetailRepository oAuthClientDetailRepository;
  private Pageable firstPageWithTwoElementsSortByClientIdDesc =
      PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "clientId"));

  @Before
  public void setUp() {
    int count = 2;
    String clientId = "client_id";

    // mock oauthClientDetails Page
    List<OAuthClientDetail> clientDetailList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      OAuthClientDetail clientDetail = new OAuthClientDetail();
      clientDetail.setClientId(clientId + " " + i);
      clientDetailList.add(clientDetail);
    }

    Page<OAuthClientDetail> clientDetails =
        new PageImpl<>(clientDetailList, firstPageWithTwoElementsSortByClientIdDesc, 10);

    Mockito.when(oAuthClientDetailRepository.findAll(firstPageWithTwoElementsSortByClientIdDesc))
        .thenReturn(clientDetails);
  }

  @Test
  public void whenFindAll_thenReturnPageableClientDetails() {
    // given
    // by setup
    // when
    Page<OAuthClientDetail> clientDetails =
        clientService.findAllOAuthClientDetails(firstPageWithTwoElementsSortByClientIdDesc);
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
          logger.debug("client id - {}", clientDetail.getClientId());
        });
  }

  @Test
  public void whenSortByClientId_thenReturnPageableSortedByClientId() {
    // given
    Pageable sortByClientId = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "clientId"));
    // when
    Page<OAuthClientDetail> clientDetails = clientService.findAllOAuthClientDetails(sortByClientId);
    // then
    ArgumentCaptor<Pageable> acPageable = ArgumentCaptor.forClass(Pageable.class);
    verify(oAuthClientDetailRepository).findAll(acPageable.capture());
    Pageable expected = acPageable.getValue();
    assertThat(expected.equals(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "clientId"))));
  }

  @TestConfiguration
  static class ClientServiceTestsContextConfiguration {
    @Bean
    public ClientService clientService() {
      return new ClientServiceImpl();
    }
  }
}
