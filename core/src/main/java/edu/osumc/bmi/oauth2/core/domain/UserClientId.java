package edu.osumc.bmi.oauth2.core.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * The association PK class for UserClient. Notice the fields names should correspond exactly to the
 * field names in the association class, but the types should be the type of the id in the
 * associated type.
 */
public class UserClientId implements Serializable {

  private static final long serialVersionUID = 1L;
  private long user;
  private long client;

  public UserClientId() {}

  public long getUser() {
    return user;
  }

  public void setUser(long user) {
    this.user = user;
  }

  public long getClient() {
    return client;
  }

  public void setClient(long client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserClientId)) return false;
    UserClientId that = (UserClientId) o;
    return getUser() == that.getUser() && getClient() == that.getClient();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUser(), getClient());
  }
}
