package edu.osumc.bmi.oauth2.service.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bmi.oauth2")
public class ServiceProperties {

  private AuthServerProperties authServer = new AuthServerProperties();
  private ClientProperties client = new ClientProperties();

  public ClientProperties getClient() {
    return client;
  }

  public void setClient(ClientProperties client) {
    this.client = client;
  }

  public AuthServerProperties getAuthServer() {
    return authServer;
  }

  public void setAuthServer(AuthServerProperties authServer) {
    this.authServer = authServer;
  }
}
