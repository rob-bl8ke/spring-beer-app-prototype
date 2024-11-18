package guru.springframework.spring_6_rest_api.services;

import guru.springframework.spring_6_rest_api.model.BeerDTO;
import guru.springframework.spring_6_rest_api.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveNewBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);
    Boolean deleteById(UUID beerId);
    Optional<BeerDTO> patchById(UUID beerId, BeerDTO beer);
}
