package edu.osumc.bmi.oauth2.service.property;

public class ClientProperties {

  private boolean redirect;

  private String redirectUri;

  public boolean isRedirect() {
    return redirect;
  }

  public void setRedirect(boolean redirect) {
    this.redirect = redirect;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }
}
