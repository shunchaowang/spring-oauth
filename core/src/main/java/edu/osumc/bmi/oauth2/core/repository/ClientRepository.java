package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.Client;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

  Optional<Client> findByOAuth2ClientId(String oauth2ClientId) throws EntityNotFoundException;
}
