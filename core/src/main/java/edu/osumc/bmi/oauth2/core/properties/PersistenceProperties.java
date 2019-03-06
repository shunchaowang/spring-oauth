package edu.osumc.bmi.oauth2.core.properties;

public class PersistenceProperties {
  private String driver = "com.mysql.cj.jdbc.Driver";
  private String url = "jdbc:mysql://127.0.0.1:3306/bmi-oauth2?useSSL=false";
  private String username = "root";
  private String password = "";

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getDriver() {
    return driver;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }
}
