package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.web.request.RegisterUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("/register")
  public DeferredResult<ResponseEntity<String>> registerUser(
      @Valid @RequestBody RegisterUserForm user, BindingResult bindingResult) {
    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    if (bindingResult.hasErrors()) {
      result.setResult(ResponseEntity.badRequest().body("Bad Request"));
      return result;
    }

    ForkJoinPool.commonPool()
        .submit(
            () -> {
              User domainUser = user.user();
              domainUser = userService.registerOAuth2User(domainUser);
              result.setResult(
                  ResponseEntity.ok().body(domainUser.getUsername() + " Registered Successfully"));
            });

    return result;
  }
}
