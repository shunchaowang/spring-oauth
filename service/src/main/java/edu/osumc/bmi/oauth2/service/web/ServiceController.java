package edu.osumc.bmi.oauth2.service.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
public class ServiceController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ServiceProperties properties;

  @GetMapping("/api/hello")
  @ResponseBody
  public String hello() {
    return "hello world!";
  }


  @GetMapping("/callback")
  public String authorizationCodeCallback(@RequestParam("code") String code) {

    logger.info("Authorization Server Code: " + code);
    // make a post to OAuth2 Authorization Server to get the tokens
    MultiValueMap<String, String> params = params(code);
    HttpHeaders headers = headers(properties.getAuthServer().getClientId(),
        properties.getAuthServer().getClientSecret());
    HttpEntity<MultiValueMap<String, String>> request = request(params, headers);
    logger.debug("Http Header Basic Authorization base64 encoded: "
        + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(
        properties.getAuthServer().getRequestTokenUrl(), HttpMethod.POST, request, String.class);

    logger.info(response.getBody());
    return "Login Successfully";
  }

  private MultiValueMap<String, String> params(String code) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(ServiceConstants.oauth2GrantType, ServiceConstants.oauth2AuthorizationCode);
    params.add(ServiceConstants.oauth2ClientId, properties.getAuthServer().getClientId());
    params.add(ServiceConstants.oauth2Code, code);
    params.add(ServiceConstants.oauth2RedirectUri, properties.getAuthServer().getRedirectUri());
    params.add(ServiceConstants.oauth2Scope, ServiceConstants.oauth2All);
    return params;
  }

  private HttpHeaders headers(String username, String password) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // String authorization =
    // "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
    // httpHeaders.add("Authorization", authorization);
    httpHeaders.setBasicAuth(username, password);
    return httpHeaders;
  }

  private HttpEntity<MultiValueMap<String, String>> request(MultiValueMap<String, String> params,
      HttpHeaders headers) {
    HttpEntity<MultiValueMap<String, String>> request =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    return request;
  }

}
