package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.OAuthClientDetailRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void whenFindAll_thenReturnPageableClientDetails() {

        // given
        int count = 2;
        String clientId = "client_id";
        List<ClientService.ClientDetail> clientDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = RandomStringUtils.random(8);
            OAuthClientDetail oAuthClientDetail = new OAuthClientDetail();
            oAuthClientDetail.setClientId(clientId + " " + i);
            ClientService.ClientDetail clientDetail = new ClientService.ClientDetail(oAuthClientDetail);
            clientDetail.setName(name);
            clientDetailList.add(clientDetail);
        }
        Pageable firstPageWithTwoElementsSortByNameDesc = PageRequest
                .of(0, 2, Sort.by(Sort.Direction.DESC, "name"));
        // when
        // then

    }
}
