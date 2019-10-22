package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.Constants;
import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.dto.UserInfo;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.RoleRepository;
import edu.osumc.bmi.oauth2.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private UserRepository userRepository;

  @Autowired private ClientRepository clientRepository;

  @Autowired private RoleRepository roleRepository;

  @Override
  @Transactional
  public User createOAuth2Admin(User user) throws EntityNotFoundException {

    Role role =
        roleRepository
            .findById(Constants.BMI_OAUTH2_SERVICE_ROLE_ADMIN_ID)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        Constants.BMI_OAUTH2_SERVICE_ROLE_ADMIN_ID + " not exists."));
    user.getRoles().add(role);

    Client client =
        clientRepository
            .findById(Constants.BMI_OAUTH2_SERVICE_ID)
            .orElseThrow(
                () -> new EntityNotFoundException(Constants.BMI_OAUTH2_SERVICE_ID + " not exists"));

    client.addUser(user);
    user.setActive(true);
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User registerOAuth2User(User user) throws EntityNotFoundException {
    Role role =
        roleRepository
            .findById(Constants.BMI_OAUTH2_SERVICE_ROLE_USER_ID)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        Constants.BMI_OAUTH2_SERVICE_ROLE_USER_ID + " not exists."));
    user.getRoles().add(role);
    Client client =
        clientRepository
            .findById(Constants.BMI_OAUTH2_SERVICE_ID)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(Constants.BMI_OAUTH2_SERVICE_ID + " not exists."));
    client.addUser(user);
    user.setActive(true);
    return userRepository.save(user);
  }

  @Override
  public User get(long id) throws EntityNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User " + id + " not exists."));
  }

  @Override
  public Optional<User> get(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Page<UserInfo> getAll(Pageable pageable) {
    return userRepository.fetchAll(pageable);
  }

  @Override
  @Transactional
  public User update(User user) throws EntityNotFoundException {
    userRepository
        .findById(user.getId())
        .orElseThrow(() -> new EntityNotFoundException(user.getUsername() + " not exists."));
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public boolean delete(User user) {
    return false;
  }

  @Override
  public Optional<Role> findRoleByName(String name) {
    return roleRepository.findByName(name);
  }
}
