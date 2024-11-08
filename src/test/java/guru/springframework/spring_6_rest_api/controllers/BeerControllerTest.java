package guru.springframework.spring_6_rest_api.controllers;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import guru.springframework.spring_6_rest_api.model.Beer;
import guru.springframework.spring_6_rest_api.services.BeerService;
import guru.springframework.spring_6_rest_api.services.BeerServiceImpl;

// These are all static imports
// ... doesn't usually display in your intelisense list so must be added manually
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Create a mock of this service. By default all members return null.
    @MockBean 
    BeerService beerService;

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();
    
    @Test
    void getBeerById() throws Exception {
        // Go fetch a pre-configured beer
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        
        // Enhance the mocked service to return this pre-configured beer given any ID
        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        
        // Run a check to ensure the response is ok and the payload is sent in JSON format.
        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID())
            .accept(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
        ;
    }
}