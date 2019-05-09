package edu.osumc.bmi.oauth2.service.web.request;

import edu.osumc.bmi.oauth2.service.web.validation.FieldMatchConstraint;

import javax.validation.constraints.NotNull;

// @FieldMatchConstraint.List({
//  @FieldMatchConstraint(
//      field = "password",
//      fieldMatch = "confirmedPassword",
//      message = "Passwords Not Match!")
// })
@FieldMatchConstraint(
    field = "password",
    fieldMatch = "confirmedPassword",
    message = "Passwords Not Match!")
public class RegisterUserForm {

  @NotNull private String username;
  @NotNull private String password;
  @NotNull private String confirmedPassword;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
