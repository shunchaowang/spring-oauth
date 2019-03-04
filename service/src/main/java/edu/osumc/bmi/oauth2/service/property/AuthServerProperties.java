package edu.osumc.bmi.oauth2.service.property;

public class AuthServerProperties {

  private String checkTokenEndpointUrl;
  private String clientId;
  private String clientSecret;
  private String requestTokenUrl;
  private String redirectUri;
  private String principalUrl;

  public String getPrincipalUrl() {
    return principalUrl;
  }

  public void setPrincipalUrl(String principalUrl) {
    this.principalUrl = principalUrl;
  }

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
}
