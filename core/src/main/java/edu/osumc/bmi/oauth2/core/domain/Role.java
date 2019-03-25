package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

  @Id @GeneratedValue private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Version private long version;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  public Role() {}

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

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return id == role.id && name.equals(role.name) && client.equals(role.client);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, client);
  }
}
