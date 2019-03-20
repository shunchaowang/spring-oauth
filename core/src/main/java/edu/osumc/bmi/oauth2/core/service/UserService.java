package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;

import java.util.List;

public interface UserService {

  User createOAuth2Admin(User user);

  User registerOAuth2User(User user);

  User get(long id);

  User get(String username);

  List<User> getAll();

  User update(User user);

  /*
   * This will deactivate user to log in.
   * Not real delete.
   * */
  boolean delete(User user);

  Role findRoleByName(String name);
}
