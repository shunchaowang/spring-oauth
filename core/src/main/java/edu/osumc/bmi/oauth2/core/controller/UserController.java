package edu.osumc.bmi.oauth2.core.controller;

import edu.osumc.bmi.oauth2.core.domain.User;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return null;
  }

  @PostMapping("/users")
  public User register(User user) {
    return null;
  }
}
