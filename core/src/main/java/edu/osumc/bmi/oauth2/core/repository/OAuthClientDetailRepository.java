package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientDetailRepository extends JpaRepository<OAuthClientDetail, String> {
  Optional<OAuthClientDetail> findByClientId(String oauth2ClientId);
}
