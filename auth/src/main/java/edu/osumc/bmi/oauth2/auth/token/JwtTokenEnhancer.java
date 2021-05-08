package edu.osumc.bmi.oauth2.auth.token;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JwtTokenEnhancer implements TokenEnhancer {
  /**
   * Provides an opportunity for customization of an access token (e.g. through its additional
   * information map) during the process of creating a new token for use by a client.
   *
   * @param accessToken the current access token with its expiration and refresh token
   * @param authentication the current authentication including client and user details
   * @return a new token enhanced with additional information
   */
  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> info = new HashMap<>();
    info.put("Department", "Department of Biomedical Informatics");
    info.put("Unit", "College of Medicine");
    info.put("Organization", "The Ohio State University");
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
    return accessToken;
  }
}
