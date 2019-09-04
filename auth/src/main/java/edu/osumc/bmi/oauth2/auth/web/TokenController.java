package edu.osumc.bmi.oauth2.auth.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(
    value = {"/oauth"},
    produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class TokenController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private DefaultTokenServices tokenServices;

  /**
   * token store must be jdbc to support revoke token.
   * the token can be uuid and jwt, as long as the token is stored in the database.
   * the token can be obtained from header Authorization or injected Authentication object.
   * @param authHeader
   * @return
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/revoke")
  public ResponseEntity<String> revokeToken(@RequestHeader(value = "Authorization") String authHeader) {

    if (StringUtils.isBlank(authHeader)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
    }

    String tokenValue = authHeader.replace("Bearer", "").trim();
    logger.info("trying to revoke {}", tokenValue);
    tokenServices.revokeToken(tokenValue);
    logger.info("revoked token {}", tokenValue);

    return ResponseEntity.status(HttpStatus.OK).body("Token revoked");
  }
//  @ResponseStatus(HttpStatus.OK)
//  public void revokeToken(Authentication authentication) {
//    final String userToken =
//        ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
//    tokenServices.revokeToken(userToken);
//  }
}
