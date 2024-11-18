package guru.springframework.spring_6_rest_api.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import guru.springframework.spring_6_rest_api.bootstrap.BootstrapData;
import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.model.BeerStyle;
import guru.springframework.spring_6_rest_api.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
public class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository
            .save(Beer.builder()
                // Beer name over 50 chars Use Jakarta @Size annotation on entity to ensure
                // a validation rather than a database integrity error.
                .beerName("***************************************************")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("564585")
                .price(new BigDecimal("11.99"))
            .build());
            
            // Ensure JPA validations
            beerRepository.flush();
        });
    }

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
