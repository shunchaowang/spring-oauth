package edu.osumc.bmi.oauth2.auth.web.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osumc.bmi.oauth2.auth.properties.AuthProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthLogoutSuccessHandler implements LogoutSuccessHandler {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private AuthProperties authProperties;

  private ObjectMapper objectMapper = new ObjectMapper();

  public AuthLogoutSuccessHandler(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    logger.info("log out success");

    if (StringUtils.isBlank(authProperties.getLogoutSuccessUrl())) {
      response.setContentType("application/json;charset=UTF-8");
      response
          .getWriter()
          .write(
              objectMapper.writeValueAsString(ResponseEntity.ok().body("Log out successfully.")));
    } else {
      logger.info("log out success url - {}", authProperties.getLogoutSuccessUrl());
      response.sendRedirect(authProperties.getLogoutSuccessUrl());
    }
  }
}
