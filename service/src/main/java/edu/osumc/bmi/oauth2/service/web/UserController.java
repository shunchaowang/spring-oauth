package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.dto.UserInfo;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.util.RequestUtils;
import edu.osumc.bmi.oauth2.service.web.request.ChangePasswordForm;
import edu.osumc.bmi.oauth2.service.web.request.RegisterUserForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@RestController
public class UserController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private UserService userService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private RequestUtils requestUtils;

  @PostMapping("/register")
  public DeferredResult<ResponseEntity<String>> registerUser(
      @Valid @RequestBody RegisterUserForm userForm, BindingResult bindingResult) {
    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    if (bindingResult.hasErrors()) {
      result.setResult(ResponseEntity.badRequest().body("Bad Request"));
      return result;
    }

    if (userService.get(userForm.getUsername()).isPresent()) {
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

  @PostMapping("/api/change-password")
  public DeferredResult<ResponseEntity<String>> changePassword(
      @Valid @RequestBody ChangePasswordForm passwordForm, BindingResult bindingResult) {

    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    if (bindingResult.hasErrors()) {
      result.setResult(ResponseEntity.badRequest().body("Bad Request"));
      return result;
    }

    String username = requestUtils.retrieveRequestUser();
    logger.info(username + " is changing the password.");
    if (StringUtils.isEmpty(username)) {
      result.setResult(ResponseEntity.status(401).body("Username does not exist"));
      logger.info("Cannot find username from the request.");
      return result;
    }

    Optional<User> userOptional = userService.get(username);
    if (!userOptional.isPresent()) {
      logger.info("Cannot find " + username);
      result.setResult(ResponseEntity.status(401).body("User does not exist"));
      return result;
    }

    User user = userOptional.get();

    if (!passwordEncoder.matches(passwordForm.getCurrentPassword(), user.getPassword())) {
      logger.info("Current password does not match.");
      result.setResult(
          ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password does not match"));
      return result;
    }

    if (passwordEncoder.matches(passwordForm.getPassword(), user.getPassword())) {
      result.setResult(
          ResponseEntity.status(406).body("Password cannot be the same with current one"));
      return result;
    }

    user.setPassword(passwordEncoder.encode(passwordForm.getPassword()));
    userService.update(user);
    result.setResult(
        ResponseEntity.status(HttpStatus.OK).body(username + " has successfully changed password"));

    return result;
  }

  @GetMapping("/api/users")
  public CompletableFuture<Page<UserInfo>> getAllUsers(final Pageable pageable) {
    return CompletableFuture.supplyAsync(() -> userService.getAll(pageable));
  }
}
