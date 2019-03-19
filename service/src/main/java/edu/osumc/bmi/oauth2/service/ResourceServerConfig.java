package edu.osumc.bmi.oauth2.service;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(ServiceProperties.class)
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private ServiceProperties serviceProperties;

  @Autowired(required = false)
  private JwtAccessTokenConverter jwtAccessTokenConverter;

  @Bean
  public RemoteTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();

    tokenServices
        .setCheckTokenEndpointUrl(serviceProperties.getAuthServer().getCheckTokenEndpointUrl());
    tokenServices.setClientId(serviceProperties.getAuthServer().getClientId());
    tokenServices.setClientSecret(serviceProperties.getAuthServer().getClientSecret());

    if (jwtAccessTokenConverter != null) {
      tokenServices.setAccessTokenConverter(jwtAccessTokenConverter);
    }

    return tokenServices;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.anonymous().disable().authorizeRequests().antMatchers("/api/**").authenticated();
  }
}
