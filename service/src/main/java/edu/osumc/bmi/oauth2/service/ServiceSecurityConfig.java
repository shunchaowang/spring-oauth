package edu.osumc.bmi.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import edu.osumc.bmi.oauth2.service.service.ServiceUserDetailsService;

@Configuration
@EnableWebSecurity
public class ServiceSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private ServiceUserDetailsService userDetailsService;

  /**
   * Used by the default implementation of {@link #authenticationManager()} to attempt to obtain an
   * {@link AuthenticationManager}. If overridden, the {@link AuthenticationManagerBuilder} should
   * be used to specify the {@link AuthenticationManager}.
   *
   * <p>
   * The {@link #authenticationManagerBean()} method can be used to expose the resulting
   * {@link AuthenticationManager} as a Bean. The {@link #userDetailsServiceBean()} can be used to
   * expose the last populated {@link UserDetailsService} that is created with the
   * {@link AuthenticationManagerBuilder} as a Bean. The {@link UserDetailsService} will also
   * automatically be populated on {@link HttpSecurity#getSharedObject(Class)} for use with other
   * {@link SecurityContextConfigurer} (i.e. RememberMeConfigurer )
   *
   * <p>
   * For example, the following configuration could be used to register in memory authentication
   * that exposes an in memory {@link UserDetailsService}:
   *
   * <pre>
   * &#064;Override
   * protected void configure(AuthenticationManagerBuilder auth) {
   *   auth
   *       // enable in memory based authentication with a user named
   *       // &quot;user&quot; and &quot;admin&quot;
   *       .inMemoryAuthentication().withUser(&quot;user&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;).and()
   *       .withUser(&quot;admin&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;, &quot;ADMIN&quot;);
   * }
   *
   * // Expose the UserDetailsService as a Bean
   * &#064;Bean
   * &#064;Override
   * public UserDetailsService userDetailsServiceBean() throws Exception {
   *   return super.userDetailsServiceBean();
   * }
   *
   * </pre>
   *
   * @param auth the {@link AuthenticationManagerBuilder} to use
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }
}
