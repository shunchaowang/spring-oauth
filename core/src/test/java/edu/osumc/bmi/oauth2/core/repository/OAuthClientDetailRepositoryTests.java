package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OAuthClientDetailRepositoryTests {

  @Autowired private OAuthClientDetailRepository oAuthClientDetailRepository;
  @Autowired private TestEntityManager entityManager;

  @Test
  public void testFindByClientId() {

    OAuthClientDetail clientDetail = new OAuthClientDetail();
    clientDetail.setClientId("client-id");
    OAuthClientDetail saved = oAuthClientDetailRepository.save(clientDetail);
    assertThat(saved != null);
    OAuthClientDetail found = oAuthClientDetailRepository.findByClientId("client-id").get();
    assertThat(found.getClientId().equals("client-id"));
  }

  @Test
  public void whenFindByClientId_thenReturnOAuthClientDetail() {

    // given
    String clientId = "client_id";
    OAuthClientDetail clientDetail = new OAuthClientDetail();
    clientDetail.setClientId(clientId);
    entityManager.persist(clientDetail);
    entityManager.flush();

    // when
    Optional<OAuthClientDetail> foundOptional =
        oAuthClientDetailRepository.findByClientId(clientId);

    // then
    assertThat(foundOptional.isPresent());
    if (foundOptional.isPresent()) {
      OAuthClientDetail found = foundOptional.get();
      assertThat(found.getClientId()).isEqualTo(clientDetail.getClientId());
    }
  }

  @Test
  public void whenFindAll_thenReturnOAuthClientDetails() {
    // given
    String clientId = "client_id";
    int count = 12;
    for (int i = 0; i < count; i++) {
      OAuthClientDetail clientDetail = new OAuthClientDetail();
      clientDetail.setClientId(clientId + " " + i);
      entityManager.persist(clientDetail);
      entityManager.flush();
    }

    // when
    List<OAuthClientDetail> clientDetails = new ArrayList<>();
    Iterable<OAuthClientDetail> clientDetailIterable = oAuthClientDetailRepository.findAll();
    //    clientDetailIterable.forEach(e -> clientDetails.add(e));
    clientDetailIterable.forEach(clientDetails::add);

    // then
    assertThat(clientDetails.size()).isEqualTo(count);
  }
}
