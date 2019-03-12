package edu.osumc.bmi.oauth2.auth.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import edu.osumc.bmi.oauth2.core.domain.Client;

public class AuthClientDetails implements ClientDetails {

  private Client client;

  public AuthClientDetails(Client client) {
    this.client = client;
  }

  @Override
  public String getClientId() {
    return client.getId() + "";
  }

  @Override
  public Set<String> getResourceIds() {
    return null;
  }

  @Override
  public boolean isSecretRequired() {
    return false;
  }

  @Override
  public String getClientSecret() {
    return null;
  }

  @Override
  public boolean isScoped() {
    return false;
  }

  @Override
  public Set<String> getScope() {
    return null;
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return null;
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return null;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return null;
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return null;
  }

  @Override
  public boolean isAutoApprove(String scope) {
    return false;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return null;
  }

}
