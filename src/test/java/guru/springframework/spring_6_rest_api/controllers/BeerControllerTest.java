package guru.springframework.spring_6_rest_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring_6_rest_api.model.BeerDTO;
import guru.springframework.spring_6_rest_api.services.BeerService;
import guru.springframework.spring_6_rest_api.services.BeerServiceImpl;

// These are all static imports
// ... doesn't usually display in your intelisense list so must be added manually
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

//
// These tests test at the controller level. The controller is the object under test.
// You should be able to set a break point somewhere in the controller method and it
// should be hit when you debug the test. The service is being "mocked" The
// "BeerServiceImpl" is a "stub" that is used only to provide stub data.

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Ask Spring Boot to provide the ObjectMapper... its a better idea.
    // Use Jackson ObjectMapper to help serialize a beer to a JSON string.
    // When performing a PATCH, PUT, POST the endpoint will be expecting a
    // JSON body. That's why this is needed. Test the controller under test
    // using a JSON payload.
    @Autowired
    ObjectMapper objectMapper;

    // Create a mock of this service. By default all members return null.
    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDTO beerDto = BeerDTO.builder().build();

        // beerService is being mocked using the annotation "@MockitoBean"
        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerDto))
            )
            .andExpect(status().isBadRequest())
            // ensure we get two validation errors...
            // Jway JsonPath is being used here in order to json structure assertions
            // see: https://github.com/json-path/JsonPath
            .andExpect(jsonPath("$.length()", is(6)))
            .andReturn();
        // Displays only the validataion data that's useful.
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class)))
            .willReturn(Optional.empty());
        
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        // beerService is being mocked using the annotation "@MockitoBean"
        given(beerService.patchById(any(), any())).willReturn(Optional.of(beer));

        // Provide the data to be patched (it is serialized below)
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Heineken");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            // payload
            .content(objectMapper.writeValueAsString(beerMap))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect((status().isNoContent()));

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        // Use ArgumentCaptor to assert that the correct field was patched.
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        // beerService is being mocked using the annotation "@MockitoBean"
        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Use 'ArgumentCaptor' to get an accurate assertion on the identifier
        // to check whether the id property is being parsed properly
        // A very handy way of asserting that values are being sent through parts of your code properly
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        // act - invoke the endpoint
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
        .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beer)))
        .andExpect(status().isNoContent());
        
        // Verify that "updateBeerById" has been called
        verify(beerService).updateBeerById(uuidArgumentCaptor.capture(), any(BeerDTO.class));
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
    }

    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("");

        // beerService is being mocked using the annotation "@MockitoBean"
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        // act - invoke the endpoint
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
        .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            // payload
            .content(objectMapper.writeValueAsString(beer)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.length()", is(1)));
    }


    @Test
    void testCreateNewBeer() throws Exception {        
        // Hard coded beer service used as a fake/stub.
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        // A new beer coming in should not have an id or version. 
        beer.setVersion(null);
        beer.setId(null);

        // beerService is being mocked using the annotation "@MockitoBean"
        // Not that "willReturn" simply returns the second beer since the beer service
        // is returning an unimportant fake beer object (and we don't care what it is).
        given(beerService.saveNewBeer(any(BeerDTO.class)))
            .willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(1));

        // act - invoke the endpoint
        // Emulating what's happening in the database so returning the "correct" beer
        // isn't important. Currently testing the basic response properties.
        mockMvc.perform(post(BeerController.BEER_PATH)
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // payload
                .content(objectMapper.writeValueAsString(beer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    // Test the GET endpoint (returning a a list of beers)
    // provides correct status, content, and content structure.
    // Note: Set a break point on the controller method...expect it
    // to be hit as the object under test is the controller method.
    @Test
    void testListBeers() throws Exception {
        // beerService is being mocked using the annotation "@MockitoBean"
        // Ensure that it returns the hard-coded beer list.
        given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25));

        // act - invoke the endpoint
        mockMvc.perform(get(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Jway JsonPath is being used here in order to json structure assertions
            // see: https://github.com/json-path/JsonPath
            .andExpect(jsonPath("$.content.length()", is(3)));
    }
    
    // Test the GET endpoint (returning a single beer)
    // provides correct status, content, and content structure.
    // Note: Set a break point on the controller method...expect it
    // to be hit as the object under test is the controller method.
    @Test
    void getBeerById() throws Exception {
        // Go fetch a pre-configured beer using this service (remember this particular 
        // service implementation returns a hard-coded list of beers).
        BeerDTO testBeer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        
        // beerService is being mocked using the annotation "@MockitoBean"
        // Configure the mocked service to return this pre-configured beer given any ID
        // Basically mocking the "getBeerById()" service call.
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
        
        // act - invoke the endpoint
        // Since a beer is returned by the mock service, the controller method should run without issue.
        // MockMvc can be used tocheck to ensure the response is ok and the payload is sent in JSON format.
        // (amongst other checks)
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Jway JsonPath is being used here in order to json structure assertions
            // see: https://github.com/json-path/JsonPath
            .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
            .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName().toString())))
            ;
    }
}