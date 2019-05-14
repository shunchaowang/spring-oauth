package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.domain.UserClient;

import java.util.List;

public interface UserClientService {

  UserClient get(long id);

  List<UserClient> findByUser(User user);

  List<UserClient> findByUser(String user);

  List<UserClient> findByClient(Client client);

  List<UserClient> findByClient(String client);

  UserClient findByUserAndClient(User user, Client client);

  UserClient findByUserAndClient(String user, String client);

  UserClient save(UserClient userClient);
}
