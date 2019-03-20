package edu.osumc.bmi.oauth2.service.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import edu.osumc.bmi.oauth2.core.aspect.Timed;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ServiceProperties properties;

  @Autowired
  private RemoteTokenServices tokenServices;

  @GetMapping("/api/hello")
  @ResponseBody
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String hello(HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    String username = "";
    if (principal instanceof UserDetails) {
      username = ((UserDetails)principal).getUsername();
    } else {
      username = principal.toString();
    }
    logger.info("username from SecurityContextHolder {}", username);
    String header = request.getHeader(ServiceConstants.HTTP_HEADER_AUTHORIZATION);
    String token = StringUtils.substringAfter(header, ServiceConstants.HTTP_HEADER_AUTHORIZATION_BEARER + " ");
    System.out.println("token: " + token);
    Claims claims =
            null;
    try {
      claims = Jwts.parser().setSigningKey(properties.getAuthServer().getJwtSigningKey().getBytes(ServiceConstants.UTF8))
      .parseClaimsJws(token).getBody();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String department = (String) claims.get(ServiceConstants.USER_NAME);
    System.out.println("department: " + department);


    return "hello world!";
  }


  @GetMapping("/login/callback")
  @Timed
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

    OAuth2Authentication authentication = tokenServices.loadAuthentication(token);
    logger.info("OAuth2Authentication {}", authentication);

    String principal = (String) authentication.getPrincipal(); // should be the username
    logger.info("username {}", principal);
    // have to inject an Authentication to the Security Context
    // OAuth2Authentication is constructed by an OAuth2Request and a
    // UsernamePasswordAuthenticationToken,
    // to inject a new Authentication into the security context, we have to create a new
    // UsernamePasswordAuthenticationToken,
    // for authorities of the authentication is immutable.

    return response;
  }

  ResponseEntity<String> requestOAuthTokens(String code) {

    MultiValueMap<String, String> params = oauthCodeParams(code);
    HttpHeaders headers = basicAuthHeaders(properties.getAuthServer().getClientId(),
        properties.getAuthServer().getClientSecret());
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    logger.debug("Http Header Basic Authorization base64 encoded: "
        + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(properties.getAuthServer().getRequestTokenUrl(), HttpMethod.POST,
        request, String.class);
  }

  //todo: tbd
  private ResponseEntity<String> requestPrincipal(String token, String tokenType) {

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, tokenType + " " + token);
    HttpEntity<?> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange("http://localhost:8080/me", HttpMethod.GET,
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
