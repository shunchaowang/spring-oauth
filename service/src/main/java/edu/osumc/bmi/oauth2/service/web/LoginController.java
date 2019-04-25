package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.aspect.Timed;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.aspect.HasRole;
import edu.osumc.bmi.oauth2.service.login.CallbackHandler;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import edu.osumc.bmi.oauth2.service.web.command.LoginForm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

@Controller
@CrossOrigin
public class LoginController {

  @Autowired ServiceProperties properties;
  @Autowired UserService userService;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private CallbackHandler callbackHandler;

  // todo testing api, will remove when released
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

  @GetMapping("/api/me")
  public DeferredResult<ResponseEntity<Map<String, String>>> profile(
      @RequestHeader HttpHeaders headers) {

    DeferredResult<ResponseEntity<Map<String, String>>> result = new DeferredResult<>();
    Map<String, String> info = new HashMap<>();
    ForkJoinPool.commonPool()
        .submit(
            () -> {
              Map user = principal(headers).getBody();
              if (user == null) {
                result.setResult(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
              } else {
                info.put("name", (String) user.get("name"));
              }
              User localUser = userService.get((String) user.get("name"));
              Set<Role> roles = localUser.getRoles();
              Set<String> localRoles = new HashSet<>();
              roles.stream()
                  .forEach(
                      (role) -> {
                        localRoles.add(role.getName());
                      });
              info.put("role", String.join(",", localRoles));
              result.setResult(ResponseEntity.status(HttpStatus.OK).body(info));
            });

    return result;
  }

  @PostMapping("/login")
  public DeferredResult<ResponseEntity<String>> requestTokenByPasswordGrantType(@RequestBody LoginForm user) {

    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();

    ForkJoinPool.commonPool()
        .submit(
            () -> {
              // header
              HttpHeaders headers =
                  basicAuthHeaders(
                      properties.getAuthServer().getClientId(),
                      properties.getAuthServer().getClientSecret());
              // body
              MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
              params.add(
                  ServiceConstants.oauth2GrantType, ServiceConstants.oauth2GrantTypePassword);
              params.add(ServiceConstants.oauth2ParamUsername, user.getUsername());
              params.add(ServiceConstants.oauth2ParamPassword, user.getPassword());
              // post to get the token using DeferredResult
              HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

              RestTemplate restTemplate = new RestTemplate();

              ResponseEntity<String> responseEntity =
                  restTemplate.exchange(
                      properties.getAuthServer().getRequestTokenUrl(),
                      HttpMethod.POST,
                      request,
                      String.class);

              result.setResult(responseEntity);
            });

    return result;
  }

  private HttpHeaders basicAuthHeaders(String username, String password) {
    return new HttpHeaders() {
      {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader =
            ServiceConstants.HTTP_AUTHORIZATION_BASIC + " " + new String(encodedAuth);
        set(ServiceConstants.HTTP_HEADER_AUTHORIZATION, authHeader);
      }
    };
  }

  // todo: tbd
  private ResponseEntity<Map> principal(String token, String tokenType) {

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, tokenType + " " + token);
    return principal(headers);
  }

  private ResponseEntity<Map> principal(HttpHeaders headers) {
    HttpEntity<?> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(
        properties.getAuthServer().getPrincipalUrl(), HttpMethod.GET, request, Map.class);
  }
}
