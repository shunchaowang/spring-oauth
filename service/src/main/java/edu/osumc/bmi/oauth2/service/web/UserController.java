package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.service.web.request.RegisterUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @PostMapping("/register")
  public DeferredResult<ResponseEntity<String>> registerUser(
      @Valid @RequestBody RegisterUserForm user, BindingResult bindingResult) {
    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
    if (bindingResult.hasErrors()) {
      result.setResult(ResponseEntity.badRequest().body("Bad Request"));
    }

    result.setResult(ResponseEntity.ok().body("Pass Binding."));

    return result;
  }
}
