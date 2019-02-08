package edu.osumc.bmi.oauth2.service;


import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(ServiceProperties.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private ServiceProperties serviceProperties;

  @Bean
  public RemoteTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();

    tokenServices.setCheckTokenEndpointUrl(serviceProperties.getOauth2().getCheckTokenEndpointUrl());
    tokenServices.setClientId(serviceProperties.getOauth2().getClientId());
    tokenServices.setClientSecret(serviceProperties.getOauth2().getClientSecret());

    return tokenServices;
  }
}
