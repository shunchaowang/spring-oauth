package edu.osumc.bmi.oauth2.auth.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotApprovedWithClientException extends UsernameNotFoundException {
  static final long serialVersionUID = 1L;

  public UserNotApprovedWithClientException(String message) {
    super(message);
  }
}
