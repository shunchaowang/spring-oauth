package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OAuthClientDetailRepositoryTests {

    @Autowired
    private OAuthClientDetailRepository oAuthClientDetailRepository;

    @Test
    public void testFindByClientId() {

        OAuthClientDetail clientDetail = new OAuthClientDetail();
        clientDetail.setClientId("client-id");
        OAuthClientDetail saved = oAuthClientDetailRepository.save(clientDetail);
        assertThat(saved != null);
        OAuthClientDetail found = oAuthClientDetailRepository.findByClientId("client-id");
        assertThat(found.getClientId().equals("client-id"));
    }
}
