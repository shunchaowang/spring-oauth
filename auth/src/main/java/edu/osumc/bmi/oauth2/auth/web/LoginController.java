package edu.osumc.bmi.oauth2.auth.web;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import edu.osumc.bmi.oauth2.auth.properties.AuthConstants;

@Controller
public class LoginController {

  @GetMapping("/welcome")
  public String welcome() {
    return "welcome";
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @GetMapping("/me")
  @ResponseBody
  public Object getPrincipal(Authentication principal) {

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      logger.info(objectMapper.writeValueAsString(principal));
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return principal;
  }

  // @GetMapping("/user/me")
  // public Map<String, String> user(Principal principal) {
  // Map<String, String> map = new LinkedHashMap<>();
  // logger.info("Principal name: " + principal.getName());
  // map.put("name", principal.getName());
  // return map;
  // }

  @GetMapping("/login")
  public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {

    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
    if (savedRequest == null) {
      // todo: throw exception
      return null;
    }

    Map<String, String[]> map0 = savedRequest.getParameterMap();
    String clientId = map0.get(AuthConstants.CLIENT_ID_PARAM_NAME)[0];
    ModelAndView mv = new ModelAndView("login");
    mv.addObject(AuthConstants.CLIENT_ID_PARAM_NAME, clientId);
    return mv;
  }

  @PostMapping("/login")
  public String performLogin(HttpServletRequest request, HttpServletResponse response) {

    SavedRequest savedRequest = (new HttpSessionRequestCache().getRequest(request, response));
    if (savedRequest == null) {
      // todo: error handling
      return null;
    }

    if (StringUtils
        .isEmpty(savedRequest.getParameterValues(AuthConstants.CLIENT_ID_PARAM_NAME)[0])) {
      // todo: error handling
      return null;
    }

    // check if client_id is matching
    if (!request.getParameter(AuthConstants.CLIENT_ID_PARAM_NAME)
        .equals(savedRequest.getParameterValues(AuthConstants.CLIENT_ID_PARAM_NAME)[0])) {
      // todo: error handling
      return null;
    }
    try {
      request.login(request.getParameter("username"), request.getParameter("password"));

    } catch (ServletException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "redirect:" + savedRequest.getRedirectUrl();
  }
}
