package edu.osumc.bmi.oauth2.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "users")
public class User implements Serializable {

  @Id @GeneratedValue private long id;

  @Column(nullable = false, length = 64, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean active;

  @Version private long version;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_clients",
      joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "client_id", nullable = false))
  private Set<Client> clients;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
  private Set<Role> roles;

  public User() {

    clients = new HashSet<>();
    roles = new HashSet<>();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public Set<Client> getClients() {
    return clients;
  }

  public void setClients(Set<Client> clients) {
    this.clients = clients;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public static UserBuilder builder = new UserBuilder();

  public static class UserBuilder {

    private String username;

    private String password;

    private boolean active;

    public UserBuilder() {}

    public UserBuilder username(String username) {
      this.username = username;
      return this;
    }

    public UserBuilder password(String password) {
      this.password = password;
      return this;
    }

    public UserBuilder active(boolean active) {
      this.active = active;
      return this;
    }

    public User build() {
      User user = new User();
      user.setActive(this.active);
      user.setUsername(this.username);
      user.setPassword(this.password);
      return user;
    }
  }
}
