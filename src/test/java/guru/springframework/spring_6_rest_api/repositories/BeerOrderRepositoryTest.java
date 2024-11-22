package guru.springframework.spring_6_rest_api.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.entities.BeerOrder;
import guru.springframework.spring_6_rest_api.entities.Customer;
import jakarta.transaction.Transactional;

// Change from DataJpaTest to SpringBootTest to populate data
// @DataJpaTest
@SpringBootTest
public class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;
    Customer testCustomer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
        testCustomer = customerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
            .customerRef("Test order")
            .customer(testCustomer)
            .build();

        // __.saveAndFlush persists immediately to the database, allowing up-to-date access
        // to lazy loaded set properties
        BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);
        System.out.println("Customer reference is " + savedBeerOrder.getCustomerRef());
    }
}
