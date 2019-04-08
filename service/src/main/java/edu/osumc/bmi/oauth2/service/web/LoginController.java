package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.aspect.Timed;
import edu.osumc.bmi.oauth2.service.aspect.HasRole;
import edu.osumc.bmi.oauth2.service.login.CallbackHandler;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class LoginController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private CallbackHandler callbackHandler;

  @GetMapping("/api/hello")
  @ResponseBody
  @HasRole("ROLE_ADMIN")
  public ResponseEntity<?> hello() {

    return ResponseEntity.ok("Hello World");
    //    return "hello world!";
  }

  @GetMapping("/login/callback")
  @Timed
  public DeferredResult<?> authorizationCodeCallback(@RequestParam("code") String code) {

    return callbackHandler.respond(code);
  }

  // todo: tbd
  private ResponseEntity<String> requestPrincipal(String token, String tokenType) {

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, tokenType + " " + token);
    HttpEntity<?> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange("http://localhost:8080/me", HttpMethod.GET, request, String.class);
  }
}
