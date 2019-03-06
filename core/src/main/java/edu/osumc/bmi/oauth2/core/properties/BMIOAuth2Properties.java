package edu.osumc.bmi.oauth2.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class BMIOAuth2Properties {

  private PersistenceProperties persistence;

  public void setPersistence(PersistenceProperties persistence) {
    this.persistence = persistence;
  }

  public PersistenceProperties getPersistence() {
    return persistence;
  }
}
