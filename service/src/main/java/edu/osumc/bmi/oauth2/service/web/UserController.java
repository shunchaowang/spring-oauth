package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.web.request.RegisterUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.concurrent.ForkJoinPool;

@RestController
public class UserController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private UserService userService;
  @Autowired private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public DeferredResult<ResponseEntity<String>> registerUser(
      @Valid @RequestBody RegisterUserForm userForm, BindingResult bindingResult) {
    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    if (bindingResult.hasErrors()) {
      result.setResult(ResponseEntity.badRequest().body("Bad Request"));
      return result;
    }

    if (usernameExists(userForm.getUsername())) {
      result.setResult(
          ResponseEntity.status(HttpStatus.CONFLICT)
              .body(userForm.getUsername() + " already exists."));
      return result;
    }

    ForkJoinPool.commonPool()
        .submit(
            () -> {
              User user = userForm.user();
              user.setPassword(passwordEncoder.encode(user.getPassword()));
              user = userService.registerOAuth2User(user);
              result.setResult(
                  ResponseEntity.status(HttpStatus.CREATED)
                      .body(user.getUsername() + " registered Successfully."));
            });

    return result;
  }

  private boolean usernameExists(String username) {
    return userService.get(username) != null;
  }
}
