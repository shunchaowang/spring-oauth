package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.dto.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public interface UserService {

  User createOAuth2Admin(User user) throws EntityNotFoundException;

  User registerOAuth2User(User user) throws EntityNotFoundException;

  User get(long id) throws EntityNotFoundException;

  Optional<User> get(String username);

  Page<UserInfo> getAll(Pageable pageable);

  User update(User user) throws EntityNotFoundException;

  /*
   * This will deactivate user to log in.
   * Not real delete.
   * */
  boolean delete(User user) throws EntityNotFoundException;

  Optional<Role> findRoleByName(String name);
}
