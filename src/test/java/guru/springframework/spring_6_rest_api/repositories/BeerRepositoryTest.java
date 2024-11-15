package guru.springframework.spring_6_rest_api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.model.BeerStyle;

@DataJpaTest
public class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // See annotations on the Beer class to see how JPA also validates before saving
        Beer savedBeer = beerRepository
            .save(Beer.builder()
                .beerName("Black Label")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("564585")
                .price(new BigDecimal("11.99"))
            .build());

        // Explicitly flush (so that you will pick up JPA entity validation errors)
        // Tells Hibernate to immediately write to the databse and will kick
        // in constraint errors.
        beerRepository.flush();
        // Hibernate does batch operations up so without the flush this may not happen
        // immediately.

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}
