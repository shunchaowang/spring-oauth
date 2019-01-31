package edu.osumc.bmi.oauth2.auth;

import edu.osumc.bmi.oauth2.auth.properties.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db-dev.properties")
@Profile("dev")
public class DevAuthDataSource implements AuthDataSource {

  @Autowired private Environment env;

  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .driverClassName(env.getProperty(AuthConstants.DB_DRIVER_KEY))
        .url(env.getProperty(AuthConstants.DB_URL_KEY))
        .username(env.getProperty(AuthConstants.DB_USERNAME_KEY))
        .password(env.getProperty(AuthConstants.DB_PASSWORD_KEY))
        .build();
  }

  @Bean
  public TokenStore tokenStore() {
    return new JdbcTokenStore(dataSource());
  }
}
