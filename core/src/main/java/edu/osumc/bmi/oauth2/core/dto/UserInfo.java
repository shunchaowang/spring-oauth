package edu.osumc.bmi.oauth2.core.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserInfo {

  @NonNull String username;
//  @NonNull boolean active;
}
