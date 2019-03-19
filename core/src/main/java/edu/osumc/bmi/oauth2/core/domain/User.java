package edu.osumc.bmi.oauth2.core.domain;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "users")
public class User implements Serializable {

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false, length = 64, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean active;

  @Version
  private long version;

  // many to many association with extra columns
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserClient> userClients;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
  private Set<Role> roles;

  public User() {

    userClients = new HashSet<>();
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

  public boolean getActive() {
    return this.active;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Client> getClientOwned() {
    return userClients.stream().filter(UserClient::isOwner)
            .map(UserClient::getClient).collect(Collectors.toSet());
  }

  public Set<Client> getClientRegistered() {
    return userClients.stream().filter(userClient -> !userClient.isOwner())
            .map(UserClient::getClient).collect(Collectors.toSet());
  }

  public static UserBuilder builder = new UserBuilder();

  public static class UserBuilder {

    private String username;

    private String password;

    private boolean active;

    public UserBuilder() {
    }

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
