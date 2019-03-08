package edu.osumc.bmi.oauth2.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class AuthProperties {

  private TokenProperties token = new TokenProperties();

  public void setToken(TokenProperties token) {
    this.token = token;
  }

  public TokenProperties getToken() {
    return token;
  }
}
