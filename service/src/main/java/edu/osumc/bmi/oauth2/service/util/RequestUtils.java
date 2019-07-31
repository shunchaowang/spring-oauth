package edu.osumc.bmi.oauth2.service.util;

import edu.osumc.bmi.oauth2.core.service.UserService;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

@Component
public class RequestUtils {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired private ServiceProperties properties;
  @Autowired private UserService userService;

  public String retrieveRequestUser() {

    String username;
    if (properties.getAuthServer().isJwtEnabled()) {
      logger.info("jwt is enabled.");
      try {
        username = parseUsernameFromJwtToken();
      } catch (Exception e) {
        logger.error(e.getMessage());
        return null;
      }
    } else {
      logger.info("jwt is disabled.");
      username = retrieveUsernameFromSecurityContext();
    }
    return username;
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

      URL url =
          getClass().getClassLoader().getResource(properties.getAuthServer().getJwtPublicKey());
      if (url == null) throw new IOException("Cert file not exist!");
      InputStream in = url.openStream();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      for (int read = 0; read != -1; read = in.read(buffer)) {
        outputStream.write(buffer, 0, read);
      }
      String pem = new String(outputStream.toByteArray());
      Pattern parser = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
      String encoded = parser.matcher(pem).replaceFirst("$1");
      byte[] publicKeySpec = Base64.getMimeDecoder().decode(encoded);
      KeyFactory keyFactory = KeyFactory.getInstance(ServiceConstants.RSA_ALGORITHM);
      PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeySpec));
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
}
