package edu.osumc.bmi.oauth2.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class BmiOauth2AuthSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically subclasses should not
   * invoke this method by calling super as it may override their configuration. The default
   * configuration is:
   *
   * <pre>
   * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
   * </pre>
   *
   * @param http the {@link HttpSecurity} to modify
   * @throws Exception if an error occurs
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic();
  }

  /**
   * Override this method to expose the {@link AuthenticationManager} from {@link
   * #configure(AuthenticationManagerBuilder)} to be exposed as a Bean. For example:
   *
   * <pre>
   * &#064;Bean(name name="myAuthenticationManager")
   * &#064;Override
   * public AuthenticationManager authenticationManagerBean() throws Exception {
   *     return super.authenticationManagerBean();
   * }
   * </pre>
   *
   * @return the {@link AuthenticationManager}
   * @throws Exception
   */
  @Override
  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
