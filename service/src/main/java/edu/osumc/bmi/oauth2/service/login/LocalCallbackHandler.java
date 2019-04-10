package edu.osumc.bmi.oauth2.service.login;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import edu.osumc.bmi.oauth2.service.web.DeferredResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.util.Map;
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
              ObjectMapper mapper = new ObjectMapper();
              Map<String, Object> responseMap = null;
              try {
                responseMap =
                    mapper.readValue(
                        response.getBody(), new TypeReference<Map<String, Object>>() {});
              } catch (JsonParseException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
              } catch (JsonMappingException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
              } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
              }

              if (responseMap == null) {
                return;
              }

              String token = (String) responseMap.get(ServiceConstants.oauth2AccessToken);
              logger.debug("access token: " + token);

              result.setResult(response);
            });

    return result;
  }
}
