package edu.osumc.bmi.oauth2.auth.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @GetMapping("/me")
  public Object getPrincipal(@AuthenticationPrincipal UserDetails principal) {

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      logger.info(objectMapper.writeValueAsString(principal));
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return principal;
  }

  @GetMapping("/hello")
  public String hello() {
    return "hello world!";
  }
}
