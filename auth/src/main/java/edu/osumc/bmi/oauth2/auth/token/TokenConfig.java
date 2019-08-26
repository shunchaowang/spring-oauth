package edu.osumc.bmi.oauth2.auth.token;

import edu.osumc.bmi.oauth2.auth.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class TokenConfig {

  @Autowired private DataSource dataSource;

  @Bean
  @ConditionalOnProperty(prefix = "bmi.oauth2.token", name = "storeType", havingValue = "jdbc")
  public TokenStore tokenStore() {
    return new JdbcTokenStore(dataSource);
  }

  @Configuration
  public static class JwtTokenConfig {

    @Autowired private AuthProperties properties;

    @Bean
    @ConditionalOnProperty(
        prefix = "bmi.oauth2.token",
        name = "storeType",
        havingValue = "jwt",
        matchIfMissing = true)
    public TokenStore jwtTokenStore() {
      return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

      if (properties.getToken().isJwtPKIEnabled()) {
        KeyStoreKeyFactory keyFactory =
            new KeyStoreKeyFactory(
                new ClassPathResource(properties.getToken().getJwtKeyStore()),
                properties.getToken().getJwtKeyStorePass().toCharArray());
        converter.setKeyPair(keyFactory.getKeyPair(properties.getToken().getJwtKeyAlias()));
      } else {
        converter.setSigningKey(properties.getToken().getJwtSigningKey());
      }

      return converter;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
      return new JwtTokenEnhancer();
    }
  }
}
