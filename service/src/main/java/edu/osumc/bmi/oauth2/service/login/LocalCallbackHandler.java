package edu.osumc.bmi.oauth2.service.login;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import edu.osumc.bmi.oauth2.service.web.DeferredResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

public class LocalCallbackHandler extends AbstractCallbackHandler {

  private ServiceProperties properties;

  private Logger logger = LoggerFactory.getLogger(getClass());

  public LocalCallbackHandler(ServiceProperties properties) {
    this.properties = properties;
  }

  @Override
  public DeferredResult<ResponseEntity<?>> respond(String code) {
    DeferredResult<ResponseEntity<?>> result = DeferredResultBuilder.INSTANCE.build();

    ForkJoinPool.commonPool()
        .submit(
            () -> {
              // make a post to OAuth2 Authorization Server to get the tokens
              ResponseEntity<String> response = requestOAuthTokens(properties, code);

              // extract access_token from response
              String token = parseAccessToken(response);
              logger.debug("access token: " + token);
              result.setResult(response);
            });

    return result;
  }
}
