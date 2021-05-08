package edu.osumc.bmi.oauth2.service.property;

public class AuthServerProperties {

  private String checkTokenEndpointUrl;
  private String clientId;
  private String clientSecret;
  private String requestTokenUrl;
  private String principalUrl;
  private String redirectUri;
  private boolean jwtEnabled = true;
  private String jwtSigningKey = "123456";
  private boolean jwtPKIEnabled = false;
  private String jwtPublicKey;

  public String getCheckTokenEndpointUrl() {
    return checkTokenEndpointUrl;
  }

  public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
    this.checkTokenEndpointUrl = checkTokenEndpointUrl;
  }

  public String getPrincipalUrl() {
    return principalUrl;
  }

  public void setPrincipalUrl(String principalUrl) {
    this.principalUrl = principalUrl;
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

  public String getRequestTokenUrl() {
    return requestTokenUrl;
  }

  public void setRequestTokenUrl(String requestTokenUrl) {
    this.requestTokenUrl = requestTokenUrl;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
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

  public boolean isJwtPKIEnabled() {
    return jwtPKIEnabled;
  }

  public void setJwtPKIEnabled(boolean jwtPKIEnabled) {
    this.jwtPKIEnabled = jwtPKIEnabled;
  }

  public String getJwtPublicKey() {
    return jwtPublicKey;
  }

  public void setJwtPublicKey(String jwtPublicKey) {
    this.jwtPublicKey = jwtPublicKey;
  }
}
