package edu.osumc.bmi.oauth2.service;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(ServiceProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private ServiceProperties serviceProperties;

  @Bean
  public RemoteTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();

    tokenServices
        .setCheckTokenEndpointUrl(serviceProperties.getAuthServer().getCheckTokenEndpointUrl());
    tokenServices.setClientId(serviceProperties.getAuthServer().getClientId());
    tokenServices.setClientSecret(serviceProperties.getAuthServer().getClientSecret());

    // test

    return tokenServices;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.anonymous().disable().authorizeRequests().antMatchers("/api/**").authenticated();
  }
}
