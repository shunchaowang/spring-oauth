package edu.osumc.bmi.oauth2.service.login;

import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.ForkJoinPool;

public class RedirectCallbackHandler extends AbstractCallbackHandler {

  private ServiceProperties properties;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public RedirectCallbackHandler(ServiceProperties properties) {
    this.properties = properties;
  }

  @Override
  public DeferredResult<RedirectView> respond(String code) {

    DeferredResult<RedirectView> result = new DeferredResult<>();

    ForkJoinPool.commonPool()
        .submit(
            () -> {

              // make a post to OAuth2 Authorization Server to get the tokens
              ResponseEntity<String> response = requestOAuthTokens(properties, code);
              // extract access_token from response
              String token = parseAccessToken(response);
              RedirectView redirectView = new RedirectView();
              redirectView.setUrl(
                  properties.getClient().getRedirectUri()
                      + "?"
                      + ServiceConstants.tokenParamName
                      + "="
                      + token);

              result.setResult(redirectView);
            });

    return result;
  }
}
