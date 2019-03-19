package edu.osumc.bmi.oauth2.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import edu.osumc.bmi.oauth2.auth.client.AuthClientDetailsService;
import edu.osumc.bmi.oauth2.auth.user.AuthUserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthClientDetailsService clientDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private TokenStore tokenStore;

  @Autowired(required = false)
  private JwtAccessTokenConverter jwtAccessTokenConverter;

  @Autowired(required = false)
  private TokenEnhancer jwtTokenEnhancer;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
    // clients.withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

    // AuthenticationManager is needed for password grant type
    endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);

    if (jwtAccessTokenConverter != null) {
      TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
      List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
      // order is important, enhancer must come before jtw converter to make sure access token contains the
      // additional info.
      if (jwtTokenEnhancer != null) {
        tokenEnhancers.add(jwtTokenEnhancer);
      }
      tokenEnhancers.add(jwtAccessTokenConverter);
      tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
      endpoints.tokenEnhancer(tokenEnhancerChain);
    }
  }

  public void configure(AuthorizationServerSecurityConfigurer authServer) {
    // check_token and token_key are both denyAll by default,
    // here to define the access level for them.
    authServer.tokenKeyAccess("permitAll()") // /oauth/token_key
        .checkTokenAccess("isAuthenticated()"); // /oauth/check_token
  }
}
