package edu.osumc.bmi.oauth2.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "oauth_client_details")
public class OAuthClientDetail implements Serializable {

  @Id @GeneratedValue private long id;

  @Column(unique = true, nullable = false)
  private String clientId;

  @Column private String clientSecret;

  @Column private String resourceIds;
}
