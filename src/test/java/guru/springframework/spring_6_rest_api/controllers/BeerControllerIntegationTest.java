package guru.springframework.spring_6_rest_api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.mappers.BeerMapper;
import guru.springframework.spring_6_rest_api.model.BeerDTO;
import guru.springframework.spring_6_rest_api.model.BeerStyle;
import guru.springframework.spring_6_rest_api.repositories.BeerRepository;
import jakarta.transaction.Transactional;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Load the entire context as this is an integration test
@SpringBootTest
public class BeerControllerIntegationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;
    // General pattern is...
    // mockMvc.perform().andExpect().andExpect()
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("showInventory", "true")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(310)))
            .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("showInventory", "false")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(310)))
            .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void testListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
            .queryParam("beerName", "IPA")
            .queryParam("beerStyle", BeerStyle.IPA.name())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(310)));
    }

    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
            .queryParam("beerName", "IPA"))
            .andExpect(status().isOk())
            .andExpect((jsonPath("$.size()", is(336)))
        );
    }

    @Test
    void testListBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
            .queryParam("beerStyle", BeerStyle.IPA.name()))
            .andExpect(status().isOk())
            .andExpect((jsonPath("$.size()", is(548)))
        );
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        // Beer name over 50 chars.
        beerMap.put("beerName", "***************************************************");

        MvcResult result = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect((status().isBadRequest()))
            .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void patchExistingBeerNotFound() {
        assertThrows(NotFoundException.class, 
            () -> beerController.patchById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Test
    @Transactional
    @Rollback
    void patchExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
        // Will pass in the id in "updatedById" and the version is auto handled.
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String beerName = "UPDATED";
        beerDto.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.patchById(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }


    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, 
            () -> beerController.deleteById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void deleteById() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void updateExistingBeerNotFound() {
        assertThrows(NotFoundException.class, 
            () -> beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Test
    @Transactional
    @Rollback
    void updateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
        // Will pass in the id in "updatedById" and the version is auto handled.
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String beerName = "UPDATED";
        beerDto.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    @Transactional
    @Rollback
    void saveNewBeerTest() {
        BeerDTO beerDto = BeerDTO.builder()
            .beerName("Castle Lager")
            .build();

        ResponseEntity responseEntity = beerController.saveNew(beerDto);

        // Assert that the response is of the correct status and have a Location header set
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        // Ensure that the Location header will navigate to an existing Beer entity
        String[] urlSegments = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUuid = UUID.fromString(urlSegments[4]);

        Beer beer = beerRepository.findById(savedUuid).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.listBeers(null, null, false);
        assertThat(dtos.size()).isEqualTo(2413);
    }

    @Transactional
    @Rollback
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers(null, null, false);
        assertThat(dtos.size()).isEqualTo(0);
    }
}
