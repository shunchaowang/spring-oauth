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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

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
  @Around("@annotation(edu.osumc.bmi.oauth2.service.aspect.HasRole) && execution(public * *(..))")
  public ResponseEntity<?> hasRole(final ProceedingJoinPoint joinPoint) {

    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    String username;

    if (properties.getAuthServer().isJwtEnabled()) {
      logger.info("jwt is enabled.");
      username = parseUsernameFromJwtToken();
    } else {
      logger.info("jwt is disabled.");
      username = retrieveUsernameFromSecurityContext();
    }

    if (StringUtils.isBlank(username)) {
      return new ResponseEntity<>("User doesn't exist.", HttpStatus.UNAUTHORIZED);
    }

    HasRole hasRole = AnnotationUtils.findAnnotation(method, HasRole.class);
    if (hasRole == null) {
      return new ResponseEntity<>("Operation is protected.", HttpStatus.FORBIDDEN);
    }

    Client client = clientService.findByOauth2ClientId(properties.getAuthServer().getClientId());
    User user = userService.get(username);
    if (hasRole.owner() && !user.getClientOwned().contains(client)) {
      return new ResponseEntity<>("User is not the owner.", HttpStatus.FORBIDDEN);
    }

    String value = hasRole.value();
    Role role = userService.findRoleByName(value);

    if (role == null) {
      return new ResponseEntity<>("Role doesn't exist.", HttpStatus.FORBIDDEN);
    }

    if (!user.getRoles().contains(role)) {
      return new ResponseEntity<>("User doesn't have the role", HttpStatus.FORBIDDEN);
    }

    ResponseEntity<?> result = null;
    try {
      result = (ResponseEntity<?>) joinPoint.proceed();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }

    return result;
  }

  private String parseUsernameFromJwtToken() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String header = request.getHeader(ServiceConstants.HTTP_HEADER_AUTHORIZATION);
    String token =
        StringUtils.substringAfter(header, ServiceConstants.HTTP_HEADER_AUTHORIZATION_BEARER + " ");
    Claims claims = Jwts.parser()
                    .setSigningKey(properties.getAuthServer().getJwtSigningKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

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
