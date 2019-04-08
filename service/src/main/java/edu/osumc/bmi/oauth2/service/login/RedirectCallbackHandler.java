package edu.osumc.bmi.oauth2.service.login;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.osumc.bmi.oauth2.service.property.ServiceConstants;
import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class RedirectCallbackHandler extends AbstractCallbackHandler {

    private ServiceProperties properties;

    public RedirectCallbackHandler(ServiceProperties properties) {
        this.properties = properties;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public DeferredResult<RedirectView> respond(String code) {

        DeferredResult<RedirectView> result = new DeferredResult<>();

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
                            RedirectView redirectView = new RedirectView();
                            redirectView.setUrl(properties.getClient().getRedirectUri()
                                            + "?" + ServiceConstants.tokenParamName + "=" + token);

                            result.setResult(redirectView);
                        });

        return result;
    }
}
