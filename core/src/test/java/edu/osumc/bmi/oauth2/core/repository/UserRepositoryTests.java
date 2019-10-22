package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.domain.User.UserBuilder;
import edu.osumc.bmi.oauth2.core.dto.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {

  @Autowired private UserRepository userRepository;

  @Test
  public void testFindByUsername() {
    User user = user();
    User found = userRepository.findById(user.getId()).get();
    assertThat(found.getUsername()).isEqualTo("tom");
  }

  @Test
  public void givenUser_whenFetchByUsername_thenReturnUserInfo() {
    User user = user();
    System.out.println("user created - " + user.getUsername() + " " + user.getId());
    User userFound = userRepository.findByUsername("tom").get();
    System.out.println("user found - " + userFound.getUsername());
    UserInfo userInfo = userRepository.fetchByUsername("tom");
    System.out.println("user fetched - " + userInfo);
  }

  private User user() {
    UserBuilder userBuilder = new UserBuilder();
    User user = userBuilder.username("tom").password("tom").active(true).build();
    return userRepository.save(user);
  }
}
