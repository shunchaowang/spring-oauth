package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.Constants;
import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.RoleRepository;
import edu.osumc.bmi.oauth2.core.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private ClientRepository clientRepository;

  @Autowired private RoleRepository roleRepository;

  @Override
  public User createOAuth2Admin(User user) {

    Role role = roleRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ROLE_ADMIN_ID);
    Client client = clientRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ID);

    user.getClients().add(client);
    user.getRoles().add(role);
    return userRepository.save(user);
  }

  @Override
  public User registerOAuth2User(User user) {
    return null;
  }

  @Override
  public User get(long id) {
    return userRepository.getOne(id);
  }

  @Override
  public User get(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<User> getAll() {
    return null;
  }

  @Override
  public User update(User user) {
    return null;
  }

  @Override
  public boolean delete(User user) {
    return false;
  }
}
