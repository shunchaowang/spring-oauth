package edu.osumc.bmi.oauth2.auth.user;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import edu.osumc.bmi.oauth2.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthUserDetailsService implements UserDetailsService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private UserService userService;

  @Autowired private ClientService clientService;

  /**
   * Locates the user based on the username. In the actual implementation, the search may possibly
   * be case sensitive, or case insensitive depending on how the implementation instance is
   * configured. In this case, the <code>UserDetails</code> object that comes back may have a
   * username that is of a different case than what was actually requested..
   *
   * @param username the username identifying the user whose data is required.
   * @return a fully populated user record (never <code>null</code>)
   * @throws UsernameNotFoundException if the user could not be found or the user has no
   *     GrantedAuthority
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    logger.info(username);
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    // String clientId = request.getParameter(AuthConstants.CLIENT_ID_PARAM_NAME);
    User user =
        userService.get(username).orElseThrow(() -> new UsernameNotFoundException(username));
    // if (!user.getClients().contains(client)) {
    // // if user has not registered to the client yet, register automatically
    // user.getClients().add(client);
    // userService.update(user);
    // }
    return new AuthUserDetails(user);
  }
}
