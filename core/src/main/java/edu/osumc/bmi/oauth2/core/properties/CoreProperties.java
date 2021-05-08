package edu.osumc.bmi.oauth2.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class CoreProperties {

  private PersistenceProperties persistence = new PersistenceProperties();

  public PersistenceProperties getPersistence() {
    return persistence;
  }

  public void setPersistence(PersistenceProperties persistence) {
    this.persistence = persistence;
  }
}
