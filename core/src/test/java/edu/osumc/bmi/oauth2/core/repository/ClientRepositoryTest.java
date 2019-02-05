package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testFindByOauth2ClientId() {
        // given
        Client client = new Client();
        client.setActive(true);
        client.setName("test-client");
        client.setOauth2ClientId("test-oauth2-client");
        clientRepository.save(client);
        // when
        Client found = clientRepository.findByOauth2ClientId("test-oauth2-client");
        // then
        assertThat(found.getOauth2ClientId().equals("test-oauth2-client"));

        found.setName("test-client-edit");
        clientRepository.save(found);
        found = clientRepository.findById(found.getId()).get();
        assertThat(found.getOauth2ClientId().equals("test-oauth2-client"));
    }
}
