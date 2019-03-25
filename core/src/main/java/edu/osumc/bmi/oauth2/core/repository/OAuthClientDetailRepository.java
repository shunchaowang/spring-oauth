package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthClientDetailRepository extends JpaRepository<OAuthClientDetail, String> {
  OAuthClientDetail findByClientId(String clientId);
}
