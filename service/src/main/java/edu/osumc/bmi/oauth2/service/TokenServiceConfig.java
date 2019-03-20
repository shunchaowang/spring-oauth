package edu.osumc.bmi.oauth2.service;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class TokenServiceConfig {

  @Autowired private ServiceProperties properties;

  @Bean
  @ConditionalOnProperty(prefix = "bmi.oauth2.authServer", name = "jwtEnabled", havingValue = "true",
          matchIfMissing = true)
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(properties.getAuthServer().getJwtSigningKey());
    return converter;
  }
}
