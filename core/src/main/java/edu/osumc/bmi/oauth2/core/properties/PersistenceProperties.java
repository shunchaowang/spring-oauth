package edu.osumc.bmi.oauth2.core.properties;

public class PersistenceProperties {
  private String driver = "com.mysql.cj.jdbc.Driver";
  private String url = "jdbc:mysql://127.0.0.1:3306/bmi-oauth2?serverTimezone=EST5EDT";
  private String username = "root";
  private String password = "";

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
