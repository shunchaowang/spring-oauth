package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.Constants;
import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.repository.ClientRepository;
import edu.osumc.bmi.oauth2.core.repository.RoleRepository;
import edu.osumc.bmi.oauth2.core.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTests {

  @Autowired private UserService userService;
  @MockBean private ClientRepository clientRepository;
  @MockBean private RoleRepository roleRepository;
  @MockBean private UserRepository userRepository;

  @Test
  public void testCreateOAuth2Admin() {
    // mock role
    when(roleRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ROLE_ADMIN_ID)).thenReturn(new Role());

    // mock client
    when(clientRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ID)).thenReturn(new Client());

    User user = User.builder.username("john").password("john").active(true).build();
    user = userService.createOAuth2Admin(user);

    assertThat(user != null);
  }

  @Test
  public void testRegisterOAuth2User() {
    // mock role
    when(roleRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ROLE_USER_ID)).thenReturn(new Role());

    // mock client
    when(clientRepository.getOne(Constants.BMI_OAUTH2_SERVICE_ID)).thenReturn(new Client());

    User user = User.builder.username("john").password("john").active(true).build();

    user = userService.registerOAuth2User(user);

    assertThat(user != null);
  }

  @TestConfiguration
  static class UserServiceTestsContextConfiguration {
    @Bean
    public UserService userService() {
      return new UserServiceImpl();
    }
  }
}
