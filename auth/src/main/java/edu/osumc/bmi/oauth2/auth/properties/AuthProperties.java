package edu.osumc.bmi.oauth2.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class AuthProperties {

  private TokenProperties token = new TokenProperties();
  private String registerUrl = "http://localhost:8000/register";
  private String logoutSuccessUrl;

  public TokenProperties getToken() {
    return token;
  }

  public void setToken(TokenProperties token) {
    this.token = token;
  }

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(String registerUrl) {
    this.registerUrl = registerUrl;
  }

  public String getLogoutSuccessUrl() {
    return logoutSuccessUrl;
  }

  public void setLogoutSuccessUrl(String logoutSuccessUrl) {
    this.logoutSuccessUrl = logoutSuccessUrl;
  }
}
