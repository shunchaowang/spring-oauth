package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.data.ClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.apache.commons.text.RandomStringGenerator;
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
        RandomStringGenerator rsg = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        // mock oauthClientDetails Page
        List<ClientDetail> clientDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ClientDetail clientDetail = new ClientDetail(rsg.generate(8), true, clientId + " " + i,
                null, null, null, null, null);
            clientDetailList.add(clientDetail);
        }

        Page<ClientDetail> clientDetails = new PageImpl<>(clientDetailList, firstPageWithTwoElementsSortByClientIdDesc, 10);

        Mockito.when(oAuthClientDetailRepository.findAllClientDetail(firstPageWithTwoElementsSortByClientIdDesc))
            .thenReturn(clientDetails);
    }

    @Test
    public void whenFindAll_thenReturnPageableClientDetails() {
        // given
        // by setup
        // when
        Page<ClientDetail> clientDetails =
            clientService.findAllClientDetails(firstPageWithTwoElementsSortByClientIdDesc);
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
                logger.debug("client id - {}", clientDetail.getClientId());
            });
    }

    @Test
    public void updatesNameToClientName() {
        // given
        Pageable sortByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "name"));
        // when
        Page<ClientDetail> clientDetails = clientService.findAllClientDetails(sortByName);
        // then
        ArgumentCaptor<Pageable> acPageable = ArgumentCaptor.forClass(Pageable.class);
        verify(oAuthClientDetailRepository).findAllClientDetail(acPageable.capture());
        Pageable expected = acPageable.getValue();
        assertThat(expected.equals(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "client.name"))));
    }

    @TestConfiguration
    static class ClientServiceTestsContextConfiguration {
        @Bean
        public ClientService clientService() {
            return new ClientServiceImpl();
        }
    }
}
