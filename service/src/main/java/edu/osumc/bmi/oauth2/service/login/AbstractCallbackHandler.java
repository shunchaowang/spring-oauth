package edu.osumc.bmi.oauth2.service.login;

import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/** */
public abstract class AbstractCallbackHandler implements CallbackHandler {

  private Logger logger = LoggerFactory.getLogger(getClass());

  protected ResponseEntity<String> requestOAuthTokens(ServiceProperties properties, String code) {

    // formulate request body
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(ServiceConstants.oauth2GrantType, ServiceConstants.oauth2AuthorizationCode);
    params.add(ServiceConstants.oauth2ClientId, properties.getAuthServer().getClientId());
    params.add(ServiceConstants.oauth2Code, code);
    params.add(ServiceConstants.oauth2RedirectUri, properties.getAuthServer().getRedirectUri());
    params.add(ServiceConstants.oauth2Scope, ServiceConstants.oauth2All);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // String authorization =
    // "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
    // headers.add("Authorization", authorization);
    headers.setBasicAuth(
        properties.getAuthServer().getClientId(), properties.getAuthServer().getClientSecret());
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    logger.debug(
        "Http Header Basic Authorization base64 encoded: "
            + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(
        properties.getAuthServer().getRequestTokenUrl(), HttpMethod.POST, request, String.class);
  }
}
