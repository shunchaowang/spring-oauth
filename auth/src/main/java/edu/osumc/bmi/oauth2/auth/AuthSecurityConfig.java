package edu.osumc.bmi.oauth2.auth;

import edu.osumc.bmi.oauth2.auth.properties.AuthProperties;
import edu.osumc.bmi.oauth2.auth.user.AuthUserDetailsService;
import edu.osumc.bmi.oauth2.auth.web.logout.AuthLogoutSuccessHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private AuthUserDetailsService userDetailsService;
  @Autowired private AuthProperties authProperties;

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
    if (StringUtils.isNotBlank(authProperties.getLogoutSuccessUrl())) {
      http.authorizeRequests().antMatchers(authProperties.getLogoutSuccessUrl()).permitAll();
    }

    http.authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        //    .logoutSuccessUrl("/logout-success").permitAll();
        .logoutSuccessHandler(logoutSuccessHandler());
  }

  /**
   * Override this method to configure {@link WebSecurity}. For example, if you wish to ignore
   * certain requests.
   *
   * @param web
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/vendors/**", "/css/**", "/js/**", "/images/**");
  }

  /**
   * Used by the default implementation of {@link #authenticationManager()} to attempt to obtain an
   * {@link AuthenticationManager}. If overridden, the {@link AuthenticationManagerBuilder} should
   * be used to specify the {@link AuthenticationManager}.
   *
   * <p>The {@link #authenticationManagerBean()} method can be used to expose the resulting {@link
   * AuthenticationManager} as a Bean. The {@link #userDetailsServiceBean()} can be used to expose
   * the last populated {@link UserDetailsService} that is created with the {@link
   * AuthenticationManagerBuilder} as a Bean. The {@link UserDetailsService} will also automatically
   * be populated on {@link HttpSecurity#getSharedObject(Class)} for use with other {@link
   * *SecurityContextConfigurer} (i.e. RememberMeConfigurer )
   *
   * <p>For example, the following configuration could be used to register in memory authentication
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
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() {
    return new AuthLogoutSuccessHandler(authProperties);
  }
}
