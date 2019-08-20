package edu.osumc.bmi.oauth2.auth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthErrorController implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * Returns the path of the error page.
   *
   * @return the error path
   */
  @Override
  public String getErrorPath() {
    return "/error";
  }

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {

    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        return "404";
      } else if (statusCode == HttpStatus.FORBIDDEN.value()
          || statusCode == HttpStatus.UNAUTHORIZED.value()) {
        return "403";
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        return "500";
      }
    }
    // logging or sth else
    return "error";
  }
}
