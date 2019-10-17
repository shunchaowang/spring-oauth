package edu.osumc.bmi.oauth2.service.aspect;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.Role;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

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
      try {
        username = parseUsernameFromJwtToken();
      } catch (Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token cannot be not verified!");
      }
    } else {
      logger.info("jwt is disabled.");
      username = retrieveUsernameFromSecurityContext();
    }

    if (StringUtils.isBlank(username)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist.");
    }

    HasRole hasRole = AnnotationUtils.findAnnotation(method, HasRole.class);
    if (hasRole == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Operation is protected.");
    }

    Optional<Client> clientOptional =
        clientService.findByOAuth2ClientId(properties.getAuthServer().getClientId());
    if (!clientOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Client not exists.");
    }

    Client client = clientOptional.get();
    Optional<User> userOptional = userService.get(username);
    if (!userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
    }
    User user = userOptional.get();
    if (hasRole.owner() && !user.getClientOwned().contains(client)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not the owner.");
    }

    String value = hasRole.value();
    Optional<Role> roleOptional = userService.findRoleByName(value);
    if (!roleOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role doesn't exist.");
    }
    Role role = roleOptional.get();

    if (!user.getRoles().contains(role)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User doesn't have the role");
    }

    ResponseEntity<?> result = null;
    try {
      result = (ResponseEntity<?>) joinPoint.proceed();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }

    return result;
  }

  private String parseUsernameFromJwtToken()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String header = request.getHeader(ServiceConstants.HTTP_HEADER_AUTHORIZATION);
    String token =
        StringUtils.substringAfter(header, ServiceConstants.HTTP_AUTHORIZATION_BEARER + " ");

    JwtParser jwtParser = Jwts.parser();
    if (properties.getAuthServer().isJwtPKIEnabled()) {
      KeyFactory keyFactory = KeyFactory.getInstance(ServiceConstants.RSA_ALGORITHM);
      PublicKey publicKey =
          keyFactory.generatePublic(
              new X509EncodedKeySpec(loadPEM(properties.getAuthServer().getJwtPublicKey())));
      jwtParser.setSigningKey(publicKey);
    } else {
      jwtParser.setSigningKey(
          properties.getAuthServer().getJwtSigningKey().getBytes(StandardCharsets.UTF_8));
    }

    Claims claims = jwtParser.parseClaimsJws(token).getBody();

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

  private byte[] loadPEM(String resource) throws IOException {
    URL url = getClass().getClassLoader().getResource(resource);
    if (url == null) throw new IOException("Cert file not exist!");
    InputStream in = url.openStream();
    String pem = new String(readAllBytes(in));
    Pattern parser = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
    String encoded = parser.matcher(pem).replaceFirst("$1");
    return Base64.getMimeDecoder().decode(encoded);
  }

  private byte[] readAllBytes(InputStream in) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    for (int read = 0; read != -1; read = in.read(buffer)) {
      outputStream.write(buffer, 0, read);
    }
    return outputStream.toByteArray();
  }
}
