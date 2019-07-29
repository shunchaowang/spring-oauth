package edu.osumc.bmi.oauth2.auth.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osumc.bmi.oauth2.auth.properties.AuthConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Controller
public class LoginController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  // todo: tbd
  // my testing apis, delete when released
//  @GetMapping("/user/me")
//  @ResponseBody
//  public Object getPrincipal(Authentication principal) {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    try {
//      logger.info(objectMapper.writeValueAsString(principal));
//    } catch (JsonProcessingException e) {
//      logger.error(e.getMessage());
//      e.printStackTrace();
//    }
//    return principal;
//  }

  /**
   * The Api to be called by resource servers to retrieve user's info
   *
   * @param principal
   * @return
   */
  @GetMapping("/me")
  public DeferredResult<ResponseEntity<Map<String, String>>> user(Principal principal) {
    DeferredResult<ResponseEntity<Map<String, String>>> result = new DeferredResult<>();

    ForkJoinPool.commonPool()
        .submit(
            () -> {
              Map<String, String> map = new LinkedHashMap<>();
              logger.info("Principal name: " + principal.getName());
              map.put("name", principal.getName());
              result.setResult(ResponseEntity.ok(map));
            });

    return result;
  }

//  @GetMapping("/login")
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

//  @PostMapping("/login")
  public String performLogin(HttpServletRequest request, HttpServletResponse response) {

    SavedRequest savedRequest = (new HttpSessionRequestCache().getRequest(request, response));
    if (savedRequest == null) {
      // todo: error handling
      return null;
    }

    if (StringUtils.isEmpty(
        savedRequest.getParameterValues(AuthConstants.CLIENT_ID_PARAM_NAME)[0])) {
      // todo: error handling
      return null;
    }

    // check if client_id is matching
    if (!request
        .getParameter(AuthConstants.CLIENT_ID_PARAM_NAME)
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
