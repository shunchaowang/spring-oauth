package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryTests {

  @Autowired TestEntityManager entityManager;
  @Autowired private ClientRepository clientRepository;
  @Autowired private UserRepository userRepository;

  @Test
  public void testFindByOAuth2ClientId() {
    // given
    Client client = new Client();
    client.setActive(true);
    client.setName("test-client");
    client.setOAuth2ClientId("test-oauth2-client");
    clientRepository.save(client);
    // when
    Client found = clientRepository.findByOAuth2ClientId("test-oauth2-client").get();
    // then
    assertThat(found.getOAuth2ClientId().equals("test-oauth2-client"));

    found.setName("test-client-edit");
    clientRepository.save(found);
    found = clientRepository.findById(found.getId()).get();
    assertThat(found.getOAuth2ClientId().equals("test-oauth2-client"));
  }

  @Test
  public void testUserAddClient() {

    User.UserBuilder userBuilder = new User.UserBuilder();
    User user = userBuilder.username("tom").password("tom").active(true).build();

    user = userRepository.save(user);
    User userCreated = userRepository.findById(user.getId()).get();

    AssertionsForClassTypes.assertThat(userCreated.getUsername().equals("tom"));

    // given
    Client client = new Client(userCreated);
    client.setActive(true);
    client.setName("test-client");
    client.setOAuth2ClientId("test-oauth2-client");
    clientRepository.save(client);
    // when
    Client clientCreated = clientRepository.findByOAuth2ClientId("test-oauth2-client").get();
    // then
    Assertions.assertThat(clientCreated.getOAuth2ClientId().equals("test-oauth2-client"));
    Assertions.assertThat(clientCreated.getOwner().getId() == userCreated.getId());
  }
}
