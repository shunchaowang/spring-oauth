package edu.osumc.bmi.oauth2.core.dto;

import edu.osumc.bmi.oauth2.core.domain.Role;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
public class UserDto {

  @NonNull String username;
  @NonNull boolean active;
  Set<Role> roles;
}
