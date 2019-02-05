package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {

  @Id @GeneratedValue private long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "oauth2_client_id", nullable = false, unique = true)
  private String oauth2ClientId;

  @Column(nullable = false)
  private boolean active;

  @Version private long version;

  @OneToMany(mappedBy = "client")
  private Set<Role> roles;

  @ManyToMany
  @JoinTable(
      name = "users_clients",
      joinColumns = @JoinColumn(name = "client_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false))
  private Set<User> users;

  public Client() {

    roles = new HashSet<>();
    users = new HashSet<>();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOauth2ClientId() {
    return oauth2ClientId;
  }

  public void setOauth2ClientId(String oauth2ClientId) {
    this.oauth2ClientId = oauth2ClientId;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Client client = (Client) o;
    return id == client.id
        && name.equals(client.name)
        && oauth2ClientId.equals(client.oauth2ClientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, oauth2ClientId);
  }
}
