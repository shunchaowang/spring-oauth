package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

@RunWith(SpringRunner.class)
public class ClientServiceIntegrationTests {

    static class ClientServiceTestsContextConfiguration {

        @Bean
        public ClientService clientService() {
            return new ClientServiceImpl();
        }
    }

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private OAuthClientDetailRepository oAuthClientDetailRepository;

    private Pageable firstPageWithTwoElementsSortByNameDesc = PageRequest
            .of(0, 2, Sort.by(Sort.Direction.DESC, "name"));
    private List<OAuthClientDetail> oAuthClientDetailList;

    @Before
    public void setUp() {
        int count = 2;
        String clientId = "client_id";

        // mock oauthClientDetails Page
        oAuthClientDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
//            String name = RandomStringUtils.random(8);
            OAuthClientDetail oAuthClientDetail = new OAuthClientDetail();
            oAuthClientDetail.setClientId(clientId + " " + i);
//            ClientService.ClientDetail clientDetail = new ClientService.ClientDetail(oAuthClientDetail);
//            clientDetail.setName(name);
            oAuthClientDetailList.add(oAuthClientDetail);
        }

        Page<OAuthClientDetail> oAuthClientDetails =
                new PageImpl<>(oAuthClientDetailList, firstPageWithTwoElementsSortByNameDesc, count);

        Mockito.when(oAuthClientDetailRepository.findAll(firstPageWithTwoElementsSortByNameDesc))
                .thenReturn(oAuthClientDetails);

        // mock client by name
        for (int i = 0; i < count; i++) {
            Client client = new Client();
            client.setName(RandomStringUtils.random(8));
            client.setOauth2ClientId(clientId + " " + i);
            Mockito.when(clientRepository.findByOauth2ClientId(clientId + " " + i))
                    .thenReturn(Optional.of(client));
        }
    }

    @Test
    public void whenFindAll_thenReturnPageableClientDetails() {

        // given


        // when
        // then

    }
}
