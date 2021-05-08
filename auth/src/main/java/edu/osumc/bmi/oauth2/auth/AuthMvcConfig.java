package edu.osumc.bmi.oauth2.auth;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthMvcConfig implements WebMvcConfigurer {

  @Bean
  @Description("Thymeleaf Message Resolver")
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

  @Bean
  @Description("Thymeleaf Layout Dialect")
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }
}
