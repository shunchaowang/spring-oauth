package edu.osumc.bmi.oauth2.service.property;

public class AuthServerProperties {

  private String checkTokenEndpointUrl;
  private String clientId;
  private String clientSecret;

  public String getCheckTokenEndpointUrl() {
    return checkTokenEndpointUrl;
  }

  public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
    this.checkTokenEndpointUrl = checkTokenEndpointUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
