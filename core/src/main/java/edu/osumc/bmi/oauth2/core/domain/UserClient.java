package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Table(name = "users_clients")
public class UserClient implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;


  @Column(nullable = false)
  private boolean owner = false;

  @Version
  private long version;

  public UserClient() {
  }

  public UserClient(User user, Client client) {
    this.user = user;
    this.client = client;
  }

  public UserClient(User user, Client client, boolean owner) {
    this.user = user;
    this.client = client;
    this.owner = owner;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Client getClient() {
    return client;
  }

  public boolean isOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }
}
