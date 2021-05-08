package edu.osumc.bmi.oauth2.auth.web;

import edu.osumc.bmi.oauth2.auth.properties.AuthConstants;
import edu.osumc.bmi.oauth2.auth.properties.AuthProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
  @Autowired private AuthProperties properties;

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

  @GetMapping("/login")
  public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {

    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
    if (savedRequest == null) {
      logger.info("The login is not coming from a redirection.");
      return new ModelAndView("403");
    }

    if (StringUtils.isBlank(savedRequest.getRedirectUrl())) {
      logger.info("The login redirection doesn't have any redirect url.");
      return new ModelAndView("403");
    }

    // store client_id in the session
    //    HttpSession session = request.getSession();
    // only parse parameter when session doesn't contain client_id
    String clientId = "";
    Map<String, String[]> params = savedRequest.getParameterMap();
    if (params.get(AuthConstants.CLIENT_ID_PARAM_NAME) != null) {
      clientId = params.get(AuthConstants.CLIENT_ID_PARAM_NAME)[0];
    }

    //    if (session.getAttribute(AuthConstants.CLIENT_ID_PARAM_NAME) == null) {
    //      if (map0.get(AuthConstants.CLIENT_ID_PARAM_NAME) != null) {
    //        session.setAttribute(AuthConstants.CLIENT_ID_PARAM_NAME,
    // map0.get(AuthConstants.CLIENT_ID_PARAM_NAME)[0]);
    //      }
    //    }
    //    String clientId = (String) session.getAttribute(AuthConstants.CLIENT_ID_PARAM_NAME);
    //    logger.info("client_id: " + session.getAttribute(AuthConstants.CLIENT_ID_PARAM_NAME));
    //
    //    // redirect_uri also needs to be stored in the session
    //    if (session.getAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME) == null) {
    //      if (map0.get(AuthConstants.REDIRECT_URI_PARAM_NAME) != null) {
    //        session.setAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME,
    // map0.get(AuthConstants.REDIRECT_URI_PARAM_NAME)[0]);
    //      }
    //    }

    //    logger.info("redirect_uri: " +
    // session.getAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME));

    ModelAndView mv = new ModelAndView("login");
    mv.addObject(AuthConstants.CLIENT_ID_PARAM_NAME, clientId);
    mv.addObject(AuthConstants.REGISTER_URL, properties.getRegisterUrl());
    return mv;
  }

  //  @PostMapping("/login")
  public String performLogin(HttpServletRequest request, HttpServletResponse response) {

    //    HttpSession session = request.getSession();
    //    logger.info("post client_id: " +
    // session.getAttribute(AuthConstants.CLIENT_ID_PARAM_NAME));
    //    logger.info("post redirect_uri: " +
    // session.getAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME));

    logger.info("in performLogin");
    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
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
    //    String redirectUri = (String) session.getAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME);
    //    session.removeAttribute(AuthConstants.CLIENT_ID_PARAM_NAME);
    //    session.removeAttribute(AuthConstants.REDIRECT_URI_PARAM_NAME);
    //    return "redirect: " + redirectUri;
  }

  @GetMapping("/logout")
  public String logout() {
    return "logout";
  }

  @GetMapping("/logout-success")
  public String logoutSuccess() {
    return "logout-success";
  }
}
