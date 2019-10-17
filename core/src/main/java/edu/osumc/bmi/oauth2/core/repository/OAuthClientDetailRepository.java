package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.data.ClientDetail;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientDetailRepository extends PagingAndSortingRepository<OAuthClientDetail, String> {
    Optional<OAuthClientDetail> findByClientId(String clientId);
//
//    @Query("select new edu.osumc.bmi.oauth2.core.data.ClientDetail(client.name, client.active, " +
//        "oAuthClientDetail.clientId, oAuthClientDetail.authorizedGrantTypes, oAuthClientDetail.webServerRedirectUri, " +
//        "oAuthClientDetail.accessTokenValidity, oAuthClientDetail.refreshTokenValidity, oAuthClientDetail.autoApprove) " +
//        "from OAuthClientDetail oAuthClientDetail " +
//        "left join Client client on oAuthClientDetail.clientId = client.oauth2ClientId " +
//        "where oAuthClientDetail.clientId = ?1")
//    Optional<ClientDetail> findClientDetailByClientId(String clientId);

//    @Query("select new edu.osumc.bmi.oauth2.core.data.ClientDetail(client.name, client.active, " +
//        "oAuthClientDetail.clientId, oAuthClientDetail.authorizedGrantTypes, oAuthClientDetail.webServerRedirectUri, " +
//        "oAuthClientDetail.accessTokenValidity, oAuthClientDetail.refreshTokenValidity, oAuthClientDetail.autoApprove) " +
//        "from OAuthClientDetail oAuthClientDetail " +
//        "left join Client client on oAuthClientDetail.clientId = client.oauth2ClientId")
//    Page<OAuthClientDetail> findAll(Pageable pageable);
}
