package edu.osumc.bmi.oauth2.service.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class ServiceProperties {

  private OAuth2Properties oauth2 = new OAuth2Properties();

  public OAuth2Properties getOauth2() {
    return oauth2;
  }

  public void setOauth2(OAuth2Properties oauth2) {
    this.oauth2 = oauth2;
  }
}
