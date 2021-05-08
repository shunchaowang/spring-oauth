package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientDetailRepository
    extends PagingAndSortingRepository<OAuthClientDetail, String> {
  Optional<OAuthClientDetail> findByClientId(String clientId);
}
