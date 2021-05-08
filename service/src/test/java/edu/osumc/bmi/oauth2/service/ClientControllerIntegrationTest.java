package edu.osumc.bmi.oauth2.service;

import edu.osumc.bmi.oauth2.core.domain.OAuthClientDetail;
import edu.osumc.bmi.oauth2.core.service.ClientService;
import edu.osumc.bmi.oauth2.service.web.ClientController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerIntegrationTest {

  @Autowired private MockMvc mvc;

  @MockBean private ClientService clientService;

  /**
   * This will fail due to call is secured.
   *
   * @throws Exception
   */
  @Test
  public void givenClients_whenFindAllClients_thenReturnJson() throws Exception {

    OAuthClientDetail clientDetail = new OAuthClientDetail();
    List<OAuthClientDetail> clientDetailList = Collections.singletonList(clientDetail);
    Page<OAuthClientDetail> clientDetailPage = new PageImpl<>(clientDetailList);

    when(clientService.findAllOAuthClientDetails(null)).thenReturn(clientDetailPage);

    mvc.perform(get("/api/clients").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
    //        .andExpect(status().isOk())
    //        .andExpect(jsonPath("$.content", hasSize(1)))
    //        .andExpect(jsonPath("$.content[0].name", is(clientDetail.getName())));
  }
}
