package edu.osumc.bmi.oauth2.core.dto;

import java.util.Set;

public interface UserInfo {

  String getUsername();

  Set<RoleInfo> getRoles();

  interface RoleInfo {
    String getName();
  }
}
