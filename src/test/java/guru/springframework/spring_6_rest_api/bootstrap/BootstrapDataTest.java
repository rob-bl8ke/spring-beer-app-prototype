package guru.springframework.spring_6_rest_api.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import guru.springframework.spring_6_rest_api.repositories.BeerRepository;
import guru.springframework.spring_6_rest_api.repositories.CustomerRepository;
import guru.springframework.spring_6_rest_api.services.BeerCsvService;
import guru.springframework.spring_6_rest_api.services.BeerCsvServiceImpl;

@DataJpaTest
// A test splice is being used (@DataJpaTest). So some dependencies are not loaded because a full component scan
// does not take place. If changed to @SpringBootTest the full context is loaded and hence all components will be seen.
// Using @Import is a light-weight approach to solving this problem. 
@Import(BeerCsvServiceImpl.class)
public class BootstrapDataTest {
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void testRun() throws Exception {
        // bootstrapData.run(null);
        bootstrapData.run(new String[] {});

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}
