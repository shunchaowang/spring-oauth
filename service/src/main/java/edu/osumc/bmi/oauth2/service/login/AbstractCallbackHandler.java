package edu.osumc.bmi.oauth2.service.login;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.Map;

/** */
public abstract class AbstractCallbackHandler implements CallbackHandler {

  private Logger logger = LoggerFactory.getLogger(getClass());

  String parseAccessToken(ResponseEntity<String> response) {

    // extract access_token from response
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> responseMap = null;
    try {
      responseMap =
          mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
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

    if (responseMap == null) {
      return null;
    }

    return (String) responseMap.get(ServiceConstants.oauth2AccessToken);
  }

  ResponseEntity<String> requestOAuthTokens(ServiceProperties properties, String code) {

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
