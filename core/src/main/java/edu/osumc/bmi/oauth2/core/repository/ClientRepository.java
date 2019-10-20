package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.dto.ClientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

  Optional<Client> findByOAuth2ClientId(String oauth2ClientId) throws EntityNotFoundException;

  @Query(
      "select new edu.osumc.bmi.oauth2.core.dto.ClientDetail(client.name, client.active, "
          + "oAuthClientDetail.clientId, oAuthClientDetail.authorizedGrantTypes, oAuthClientDetail.webServerRedirectUri, "
          + "oAuthClientDetail.accessTokenValidity, oAuthClientDetail.refreshTokenValidity, oAuthClientDetail.autoApprove) "
          + "from OAuthClientDetail oAuthClientDetail "
          + "left join Client client on oAuthClientDetail.clientId = client.oAuth2ClientId "
          + "where oAuthClientDetail.clientId = ?1")
  Optional<ClientDetail> findClientDetailByClientId(String clientId);

  @Query(
      "select new edu.osumc.bmi.oauth2.core.dto.ClientDetail(client.name, client.active, "
          + "oAuthClientDetail.clientId, oAuthClientDetail.authorizedGrantTypes, oAuthClientDetail.webServerRedirectUri, "
          + "oAuthClientDetail.accessTokenValidity, oAuthClientDetail.refreshTokenValidity, oAuthClientDetail.autoApprove) "
          + "from OAuthClientDetail oAuthClientDetail "
          + "left join Client client on oAuthClientDetail.clientId = client.oAuth2ClientId")
  Page<ClientDetail> findAllClientDetails(Pageable pageable);
}
