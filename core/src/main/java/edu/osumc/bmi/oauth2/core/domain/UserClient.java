package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

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

  @Version private long version;

  public UserClient() {}

  public UserClient(User user, Client client) {
    this.user = user;
    this.client = client;
  }

  public UserClient(User user, Client client, boolean owner) {
    this.user = user;
    this.client = client;
    this.owner = owner;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public boolean isOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of hash
   * tables such as those provided by {@link HashMap}.
   *
   * <p>The general contract of {@code hashCode} is:
   *
   * <ul>
   *   <li>Whenever it is invoked on the same object more than once during an execution of a Java
   *       application, the {@code hashCode} method must consistently return the same integer,
   *       provided no information used in {@code equals} comparisons on the object is modified.
   *       This integer need not remain consistent from one execution of an application to another
   *       execution of the same application.
   *   <li>If two objects are equal according to the {@code equals(Object)} method, then calling the
   *       {@code hashCode} method on each of the two objects must produce the same integer result.
   *   <li>It is <em>not</em> required that if two objects are unequal according to the {@link
   *       Object#equals(Object)} method, then calling the {@code hashCode} method on each of the
   *       two objects must produce distinct integer results. However, the programmer should be
   *       aware that producing distinct integer results for unequal objects may improve the
   *       performance of hash tables.
   * </ul>
   *
   * <p>As much as is reasonably practical, the hashCode method defined by class {@code Object} does
   * return distinct integers for distinct objects. (The hashCode may or may not be implemented as
   * some function of an object's memory address at some point in time.)
   *
   * @return a hash code value for this object.
   * @see Object#equals(Object)
   * @see System#identityHashCode
   */
  @Override
  public int hashCode() {
    return Objects.hash(user, client);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * <p>The {@code equals} method implements an equivalence relation on non-null object references:
   *
   * <ul>
   *   <li>It is <i>reflexive</i>: for any non-null reference value {@code x}, {@code x.equals(x)}
   *       should return {@code true}.
   *   <li>It is <i>symmetric</i>: for any non-null reference values {@code x} and {@code y}, {@code
   *       x.equals(y)} should return {@code true} if and only if {@code y.equals(x)} returns {@code
   *       true}.
   *   <li>It is <i>transitive</i>: for any non-null reference values {@code x}, {@code y}, and
   *       {@code z}, if {@code x.equals(y)} returns {@code true} and {@code y.equals(z)} returns
   *       {@code true}, then {@code x.equals(z)} should return {@code true}.
   *   <li>It is <i>consistent</i>: for any non-null reference values {@code x} and {@code y},
   *       multiple invocations of {@code x.equals(y)} consistently return {@code true} or
   *       consistently return {@code false}, provided no information used in {@code equals}
   *       comparisons on the objects is modified.
   *   <li>For any non-null reference value {@code x}, {@code x.equals(null)} should return {@code
   *       false}.
   * </ul>
   *
   * <p>The {@code equals} method for class {@code Object} implements the most discriminating
   * possible equivalence relation on objects; that is, for any non-null reference values {@code x}
   * and {@code y}, this method returns {@code true} if and only if {@code x} and {@code y} refer to
   * the same object ({@code x == y} has the value {@code true}).
   *
   * <p>Note that it is generally necessary to override the {@code hashCode} method whenever this
   * method is overridden, so as to maintain the general contract for the {@code hashCode} method,
   * which states that equal objects must have equal hash codes.
   *
   * @param obj the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
   * @see #hashCode()
   * @see HashMap
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    UserClient userClient = (UserClient) obj;
    return user.equals(userClient.getUser()) && client.equals(userClient.getClient());
  }
}