package edu.osumc.bmi.oauth2.core.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.domain.User.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindByUsername() {

    UserBuilder userBuilder = new UserBuilder();

    User user = userBuilder.username("tom")
        .password("tom")
        .active(true)
        .build();

    user = userRepository.save(user);
    User found = userRepository.getOne(user.getId());

    assertThat(found.getUsername().equals("tom"));
  }
}
