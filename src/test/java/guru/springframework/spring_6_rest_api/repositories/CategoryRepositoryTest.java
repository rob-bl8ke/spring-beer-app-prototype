package guru.springframework.spring_6_rest_api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.entities.Category;

@SpringBootTest
public class CategoryRepositoryTest {
    
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;
    Beer testBeer;
    
    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    void testAddCategory() {
        Category savedCategory = categoryRepository.saveAndFlush(Category.builder()
            .description("Ales")
            .build());
        assertThat(savedCategory.getId()).isNotNull();
    }
}
