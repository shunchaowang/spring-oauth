package edu.osumc.bmi.oauth2.core.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

@RunWith(SpringRunner.class)
public class UserServiceTests {

  @TestConfiguration
  static class UserServiceTestsContextConfiguration {
    @Bean
    public UserService userService() {
      return new UserServiceImpl();
    }
  }

  @Autowired private UserService userService;

  @MockBean private UserRepository userRepository;
  @MockBean private ClientRepository clientRepository;
  @MockBean private RoleRepository roleRepository;

  @Test
  public void testCreateOAuth2Admin() {
    User user = User.builder.username("john").password("john").active(true).build();

    user = userService.createOAuth2Admin(user);

    assertThat(user != null);
  }
}
