package edu.osumc.bmi.oauth2.service;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(ServiceProperties.class)
@EnableAspectJAutoProxy
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired private ServiceProperties serviceProperties;

  @Bean
  public RemoteTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();

    tokenServices.setCheckTokenEndpointUrl(
        serviceProperties.getAuthServer().getCheckTokenEndpointUrl());
    tokenServices.setClientId(serviceProperties.getAuthServer().getClientId());
    tokenServices.setClientSecret(serviceProperties.getAuthServer().getClientSecret());

    return tokenServices;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // cors needs to be enabled here, this HttpSecurity overrides global CorsRegistry.
    // /api/** should also allow cross origin request.
    http.cors()
        .and()
        .anonymous()
        .disable()
        .authorizeRequests()
        .antMatchers("/api/**")
        .authenticated();
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      /**
       * Configure cross origin requests processing. Enable cors support globally.
       *
       * @param registry
       * @since 4.2
       */
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
      }
    };
  }
}
