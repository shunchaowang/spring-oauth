package edu.osumc.bmi.oauth2.auth.properties;

public class TokenProperties {

  private String storeType;

  private boolean jwtPKIEnabled = false;

  private String jwtKeyStore;
  private String jwtKeyStorePass;
  private String jwtKeyAlias;

  private String jwtSigningKey = "123456";

  public boolean isJwtPKIEnabled() {
    return jwtPKIEnabled;
  }

  public void setJwtPKIEnabled(boolean jwtPKIEnabled) {
    this.jwtPKIEnabled = jwtPKIEnabled;
  }

  public String getJwtKeyStore() {
    return jwtKeyStore;
  }

  public void setJwtKeyStore(String jwtKeyStore) {
    this.jwtKeyStore = jwtKeyStore;
  }

  public String getJwtKeyStorePass() {
    return jwtKeyStorePass;
  }

  public void setJwtKeyStorePass(String jwtKeyStorePass) {
    this.jwtKeyStorePass = jwtKeyStorePass;
  }

  public String getJwtKeyAlias() {
    return jwtKeyAlias;
  }

  public void setJwtKeyAlias(String jwtKeyAlias) {
    this.jwtKeyAlias = jwtKeyAlias;
  }

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
