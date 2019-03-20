package edu.osumc.bmi.oauth2.service.property;

public class AuthServerProperties {

  private String checkTokenEndpointUrl;
  private String clientId;
  private String clientSecret;
  private String requestTokenUrl;
  private String redirectUri;
  private boolean jwtEnabled = true;
  private String jwtSigningKey = "123456";

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

  public void setRequestTokenUrl(String requestTokenUrl) {
    this.requestTokenUrl = requestTokenUrl;
  }

  public String getRequestTokenUrl() {
    return requestTokenUrl;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public String getJwtSigningKey() {
    return jwtSigningKey;
  }

  public void setJwtSigningKey(String jwtSigningKey) {
    this.jwtSigningKey = jwtSigningKey;
  }

  public boolean isJwtEnabled() {
    return jwtEnabled;
  }

  public void setJwtEnabled(boolean jwtEnabled) {
    this.jwtEnabled = jwtEnabled;
  }
}
