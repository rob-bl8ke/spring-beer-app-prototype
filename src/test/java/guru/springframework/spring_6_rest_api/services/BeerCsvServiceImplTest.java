package guru.springframework.spring_6_rest_api.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import guru.springframework.spring_6_rest_api.model.BeerCSVRecord;

public class BeerCsvServiceImplTest {
    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCsv() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> recs = beerCsvService.convertCsv(file);
        
        System.out.println(recs.size());
        assertThat(recs.size()).isGreaterThan(0);
    }
}
