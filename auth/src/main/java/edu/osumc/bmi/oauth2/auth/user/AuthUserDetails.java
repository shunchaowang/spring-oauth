package edu.osumc.bmi.oauth2.auth.user;

import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails implements UserDetails {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private User user;

  public AuthUserDetails(User user) {
    logger.info("" + user.getId());
    this.user = user;
  }

  /**
   * Returns the authorities granted to the user. Cannot return <code>null</code>.
   *
   * @return the authorities, sorted by natural key (never <code>null</code>)
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : user.getRoles()) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
      authorities.add(authority);
    }
    return authorities;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /**
   * Returns the username used to authenticate the user. Cannot return <code>null</code>.
   *
   * @return the username (never <code>null</code>)
   */
  @Override
  public String getUsername() {
    return user.getUsername();
  }

  /**
   * Indicates whether the user's account has expired. An expired account cannot be authenticated.
   *
   * @return <code>true</code> if the user's account is valid (ie non-expired), <code>false</code>
   *     if no longer valid (ie expired)
   */
  @Override
  public boolean isAccountNonExpired() {
    return user.isActive();
  }

  /**
   * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
   *
   * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return user.isActive();
  }

  /**
   * Indicates whether the user's credentials (password) has expired. Expired credentials prevent
   * authentication.
   *
   * @return <code>true</code> if the user's credentials are valid (ie non-expired), <code>false
   *     </code> if no longer valid (ie expired)
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return user.isActive();
  }

  /**
   * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
   *
   * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
   */
  @Override
  public boolean isEnabled() {
    return user.isActive();
  }
}
