package edu.osumc.bmi.oauth2.service.web;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired private ClientService clientService;

  @GetMapping("/api/clients")
  public Page<OAuthClientDetail> getAllClients(Pageable pageable) {
    return clientService.findAllOAuthClientDetails(pageable);
  }

  @GetMapping("/api/clients/{oauth2ClientId}")
  public OAuthClientDetail getClient(@PathVariable String oauth2ClientId) {
    return clientService.getOAuthClientDetail(oauth2ClientId).orElse(null);
  }
}
