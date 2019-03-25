package edu.osumc.bmi.oauth2.auth.client;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthClientDetails implements ClientDetails {

  private OAuthClientDetail clientDetail;

  public AuthClientDetails(OAuthClientDetail clientDetail) {
    this.clientDetail = clientDetail;
  }

  @Override
  public String getClientId() {
    return clientDetail.getClientId();
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
    return clientDetail.getClientSecret();
  }

  @Override
  public boolean isScoped() {
    return false;
  }

  @Override
  public Set<String> getScope() {
    if (StringUtils.isBlank(clientDetail.getScope())) return null;
    return new HashSet<>(Arrays.asList(StringUtils.split(clientDetail.getScope(), ",")));
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    if (StringUtils.isBlank(clientDetail.getAuthorizedGrantTypes()))return null;
    return new HashSet<>(Arrays.asList(StringUtils.split(clientDetail.getAuthorizedGrantTypes(), ",")));
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    if (StringUtils.isBlank(clientDetail.getWebServerRedirectUri())) return null;
//    return Set.of(StringUtils.split(clientDetail.getWebServerRedirectUri(), ","));
      return new HashSet<>(Arrays.asList(StringUtils.split(clientDetail.getWebServerRedirectUri(), ",")));
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    if (StringUtils.isBlank(clientDetail.getAuthorities()))return null;
    Collection<GrantedAuthority> authorities = new HashSet<>();
    Arrays.stream(StringUtils.split(clientDetail.getAuthorities(), ",")).forEach((auth) -> {
      authorities.add(new SimpleGrantedAuthority(auth));
    });
    return authorities;
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return clientDetail.getAccessTokenValidity();
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return clientDetail.getRefreshTokenValidity();
  }

  @Override
  public boolean isAutoApprove(String scope) {
    return "1".equals(clientDetail.getAutoApprove()) || Boolean.valueOf(clientDetail.getAutoApprove());
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return null;
  }
}
