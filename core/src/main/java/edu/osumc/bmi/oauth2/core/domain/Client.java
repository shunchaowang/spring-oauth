package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

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

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private Set<UserClient> userClients;

  public Client() {
    roles = new HashSet<>();
    userClients = new HashSet<>();
  }

  public Client(User owner) {
    roles = new HashSet<>();
    userClients = new HashSet<>();
    UserClient userClient = new UserClient(owner, this, true);
    userClients.add(userClient);
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

  public boolean getActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Get the owner of the client.
   *
   * @return the owner of the client, should be only one.
   */
  public User getOwner() {
    UserClient userClient =
        userClients.stream().filter(UserClient::isOwner).findFirst().orElse(null);
    return userClient == null ? null : userClient.getUser();
  }

  /**
   * Get all users of the client except the owner.
   *
   * @return all users registered on the client except the owner, null if no one has registered.
   */
  public Set<User> getUsers() {
    return userClients.stream()
        .filter(UserClient::isOwner)
        .map(UserClient::getUser)
        .collect(Collectors.toSet());
  }

  /**
   * Add a user to the client, the user cannot be the owner.
   *
   * @param user
   */
  public void addUser(User user) {
    if (userClients == null) userClients = new HashSet<>();
    UserClient userClient = new UserClient(user, this);
    userClients.add(userClient);
  }

  /**
   * Remove a user from the client, the user cannot be the owner.
   *
   * @param user to removed, cannot be the owner
   */
  public void removeUser(User user) {
    UserClient userClient = new UserClient(user, this);
    if (!userClients.contains(userClient)) return;
    userClients.remove(userClient);
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
