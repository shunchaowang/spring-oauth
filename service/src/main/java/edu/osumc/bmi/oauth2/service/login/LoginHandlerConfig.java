package edu.osumc.bmi.oauth2.service.login;

import edu.osumc.bmi.oauth2.service.property.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class LoginHandlerConfig {

    @Autowired
    private ServiceProperties properties;

    @Bean
    @ConditionalOnProperty(prefix = "bmi.oauth2.client", name = "redirect", havingValue = "false", matchIfMissing = true)
    public CallbackHandler callbackHandler() {return new LocalCallbackHandler(properties);}

    @Bean
    @ConditionalOnProperty(prefix = "bmi.oauth2.client", name = "redirect", havingValue = "true")
    public CallbackHandler redirectCallbackHandler() {
        return new RedirectCallbackHandler(properties);
    }
}
