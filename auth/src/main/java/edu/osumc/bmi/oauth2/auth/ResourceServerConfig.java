package edu.osumc.bmi.oauth2.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    //    http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
    http.requestMatchers()
        .antMatchers("/me", "/oauth/revoke")
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated();
  }
}
