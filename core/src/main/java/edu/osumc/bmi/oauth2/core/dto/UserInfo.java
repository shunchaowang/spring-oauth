package edu.osumc.bmi.oauth2.core.dto;

import edu.osumc.bmi.oauth2.core.domain.Role;
import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class UserInfo {

  private final String username;
//  @NonNull boolean active;
  private final String role;

  public UserInfo(String username, Collection<Role> roles) {
    this.username = username;
    role = roles.stream().map(Role::getName).collect(Collectors.joining());
  }
}
