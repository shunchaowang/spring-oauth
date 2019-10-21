package edu.osumc.bmi.oauth2.core.dto;

import edu.osumc.bmi.oauth2.core.domain.Role;

import java.util.Collection;

public interface UserInfo {

    String getUsername();
    boolean getActive();
    Collection<Role> getRoles();

}
