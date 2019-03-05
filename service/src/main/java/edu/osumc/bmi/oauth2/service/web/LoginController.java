package edu.osumc.bmi.oauth2.service.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class LoginController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ServiceProperties properties;

  @GetMapping("/api/hello")
  @ResponseBody
  public String hello() {
    return "hello world!";
  }


  @GetMapping("/login/callback")
  public ResponseEntity<String> authorizationCodeCallback(@RequestParam("code") String code) {

    logger.info("Authorization Server Code: " + code);

    // make a post to OAuth2 Authorization Server to get the tokens
    ResponseEntity<String> response = requestOAuthTokens(code);

    // extract access_token from response
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> responseMap = null;
    try {
      responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
      });
    } catch (JsonParseException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    } catch (JsonMappingException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }

    String token = (String) responseMap.get(ServiceConstants.oauth2AccessToken);
    String tokenType = (String) responseMap.get(ServiceConstants.oauth2TokenType);
    // request user info from oauth2 authorization server
    // check if user exists in service, register the user if not, pull user roles if user has
    // already registered.
    ResponseEntity<String> principalEntity = requestPrincipal(token, tokenType);
    String principal = principalEntity.getBody();
    return response;
  }

  ResponseEntity<String> requestOAuthTokens(String code) {

    MultiValueMap<String, String> params = oauthCodeParams(code);
    HttpHeaders headers = basicAuthHeaders(properties.getAuthServer().getClientId(),
        properties.getAuthServer().getClientSecret());
    HttpEntity<MultiValueMap<String, String>> request =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    logger.debug("Http Header Basic Authorization base64 encoded: "
        + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(properties.getAuthServer().getRequestTokenUrl(), HttpMethod.POST,
        request, String.class);
  }

  private ResponseEntity<String> requestPrincipal(String token, String tokenType) {

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, tokenType + " " + token);
    HttpEntity<?> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(properties.getAuthServer().getPrincipalUrl(), HttpMethod.GET,
        request, String.class);
  }

  private MultiValueMap<String, String> oauthCodeParams(String code) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(ServiceConstants.oauth2GrantType, ServiceConstants.oauth2AuthorizationCode);
    params.add(ServiceConstants.oauth2ClientId, properties.getAuthServer().getClientId());
    params.add(ServiceConstants.oauth2Code, code);
    params.add(ServiceConstants.oauth2RedirectUri, properties.getAuthServer().getRedirectUri());
    params.add(ServiceConstants.oauth2Scope, ServiceConstants.oauth2All);
    return params;
  }

  private HttpHeaders basicAuthHeaders(String username, String password) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // String authorization =
    // "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
    // httpHeaders.add("Authorization", authorization);
    httpHeaders.setBasicAuth(username, password);
    return httpHeaders;
  }
}