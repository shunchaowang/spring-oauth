package edu.osumc.bmi.oauth2.service.web.request;

import edu.osumc.bmi.oauth2.service.web.validation.FieldMatchConstraint;

import javax.validation.constraints.NotNull;

@FieldMatchConstraint(
    field = "password",
    fieldMatch = "confirmedPassword",
    message = "Passwords Not Match!")
public class ChangePasswordForm {

  @NotNull private String currentPassword;
  @NotNull private String password;
  @NotNull private String confirmedPassword;

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmedPassword() {
    return confirmedPassword;
  }

  public void setConfirmedPassword(String confirmedPassword) {
    this.confirmedPassword = confirmedPassword;
  }
}
