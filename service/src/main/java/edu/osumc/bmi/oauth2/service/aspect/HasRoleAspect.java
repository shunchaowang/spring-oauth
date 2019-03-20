package edu.osumc.bmi.oauth2.service.aspect;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

@Aspect
@Component
public class HasRoleAspect {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private ServiceProperties properties;

  @Autowired private UserService userService;

  @Autowired private ClientService clientService;

  // get username from jwt token or security context
  // load user role from database
  // check if user has role specified by the annotation
  @Before("@annotation(edu.osumc.bmi.oauth2.service.aspect.HasRole) && execution(public * *(..))")
  public void hasRole(final JoinPoint joinPoint) throws UnauthorizedUserException {

    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    String username = null;

    if (properties.getAuthServer().isJwtEnabled()) {
      logger.info("jwt is enabled.");
      username = parseUsernameFromJwtToken();
    } else {
      logger.info("jwt is disabled.");
      username = retrieveUsernameFromSecurityContext();
    }

    if (StringUtils.isBlank(username)) {
      throw new UnauthorizedUserException("User doesn't exist.");
    }

    HasRole hasRole = AnnotationUtils.findAnnotation(method, HasRole.class);
    if (hasRole == null) {
      return;
    }

    Client client = clientService.findByOauth2ClientId(properties.getAuthServer().getClientId());
    User user = userService.get(username);
    if (hasRole.owner() && !user.getClientOwned().contains(client)) {
      throw new UnauthorizedUserException("User doesn't own the client.");
    }

    String value = hasRole.value();
    Role role = userService.findRoleByName(value);

    if (role == null) {
      throw new UnauthorizedUserException("Role doesn't exist.");
    }

    if (!user.getRoles().contains(role)) {
      throw new UnauthorizedUserException("User doesn't have the role.");
    }
  }

  private String parseUsernameFromJwtToken() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String header = request.getHeader(ServiceConstants.HTTP_HEADER_AUTHORIZATION);
    String token =
        StringUtils.substringAfter(header, ServiceConstants.HTTP_HEADER_AUTHORIZATION_BEARER + " ");
    Claims claims = null;
    try {
      claims =
          Jwts.parser()
              .setSigningKey(
                  properties.getAuthServer().getJwtSigningKey().getBytes(ServiceConstants.UTF8))
              .parseClaimsJws(token)
              .getBody();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return (String) claims.get(ServiceConstants.USER_NAME);
  }

  private String retrieveUsernameFromSecurityContext() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else {
      return principal.toString();
    }
  }
}
