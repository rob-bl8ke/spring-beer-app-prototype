package guru.springframework.spring_6_rest_api.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.entities.Customer;
import guru.springframework.spring_6_rest_api.model.BeerStyle;
import guru.springframework.spring_6_rest_api.repositories.BeerRepository;
import guru.springframework.spring_6_rest_api.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepostory;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                .name("Rob Douglas")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
            
            Customer customer2 = Customer.builder()
                .name("Jen Daly")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
            
            Customer customer3 = Customer.builder()
                .name("Shaun Roberts")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
            
            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        }
    }

    private void loadBeerData() {
        if (beerRepostory.count() == 0) {
            Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("111111")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
                
            Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.IPA)
                .upc("222222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
                
            Beer beer3 = Beer.builder()
                .beerName("Jack Black")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("333333")
                .price(new BigDecimal("15.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
                
                beerRepostory.saveAll(Arrays.asList(beer1, beer2, beer3));
        }
    }
}
