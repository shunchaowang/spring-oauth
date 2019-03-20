package edu.osumc.bmi.oauth2.auth.properties;

public class TokenProperties {

  private String storeType;

  private String jwtSigningKey = "123456";

  public String getStoreType() {
    return storeType;
  }

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  public String getJwtSigningKey() {
    return jwtSigningKey;
  }

  public void setJwtSigningKey(String jwtSigningKey) {
    this.jwtSigningKey = jwtSigningKey;
  }
}
