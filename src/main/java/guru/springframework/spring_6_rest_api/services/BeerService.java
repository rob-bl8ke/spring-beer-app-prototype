package guru.springframework.spring_6_rest_api.services;

import guru.springframework.spring_6_rest_api.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();
    Optional<Beer> getBeerById(UUID id);
    Beer saveNewBeer(Beer beer);
    void updateBeerById(UUID beerId, Beer beer);
    void deleteById(UUID beerId);
    void patchById(UUID beerId, Beer beer);
}
