package edu.osumc.bmi.oauth2.core;

import edu.osumc.bmi.oauth2.core.properties.CoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

;

@Configuration
@EnableConfigurationProperties(CoreProperties.class)
public class CoreBeanConfig {

  @Autowired private CoreProperties properties;

  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .driverClassName(properties.getPersistence().getDriver())
        .url(properties.getPersistence().getUrl())
        .username(properties.getPersistence().getUsername())
        .password(properties.getPersistence().getPassword())
        .build();
  }
}
