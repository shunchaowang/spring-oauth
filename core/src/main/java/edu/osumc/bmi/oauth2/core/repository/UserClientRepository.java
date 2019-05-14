package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.domain.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long> {

  List<UserClient> findByUser(User user);

  List<UserClient> findByUserUsername(String user);

  List<UserClient> findByClient(Client client);

  List<UserClient> findByClientName(String client);

  UserClient findByUserAndClient(User user, Client client);

  UserClient findByUserUsernameAndClientName(String user, String client);
}
