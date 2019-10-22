package edu.osumc.bmi.oauth2.core.dto;

import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import lombok.Value;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserInfo {

  private String username;
  private boolean active;
  private String role;

  public UserInfo(User user, Collection<Role> roles) {
    this.username = user.getUsername();
    this.active = user.getActive();
    this.role = roles.stream().map(Role::getName).collect(Collectors.joining());
  }
}
