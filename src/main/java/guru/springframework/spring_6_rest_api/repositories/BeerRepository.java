package guru.springframework.spring_6_rest_api.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.model.BeerStyle;


// Spring data will provide implementations for this interface for use at runtime.
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    
    // Note there is no implementation here because this conforms to Spring Data JPA naming conventions
    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
    List<Beer> findByBeerStyle(BeerStyle beerStyle);
    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
}
