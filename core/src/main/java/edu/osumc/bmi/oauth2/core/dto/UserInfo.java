package edu.osumc.bmi.oauth2.core.dto;

import java.util.Set;
import java.util.stream.Collectors;

public interface UserInfo {

  String getUsername();

  boolean getActive();

  Set<RoleInfo> getRoles();

  default String getRoleName() {
    return getRoles().stream().map(RoleInfo::getName).collect(Collectors.joining("|"));
  }

  interface RoleInfo {
    String getName();
  }
}
