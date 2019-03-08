package edu.osumc.bmi.oauth2.auth.properties;

public class TokenProperties {

  private String storeType;

  private String jwtSigningKey = "123456";

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  public String getStoreType() {
    return storeType;
  }

  public void setJwtSigningKey(String jwtSigningKey) {
    this.jwtSigningKey = jwtSigningKey;
  }

  public String getJwtSigningKey() {
    return jwtSigningKey;
  }
}
