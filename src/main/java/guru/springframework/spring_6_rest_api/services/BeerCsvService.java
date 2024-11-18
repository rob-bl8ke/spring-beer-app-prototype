package guru.springframework.spring_6_rest_api.services;

import java.io.File;
import java.util.List;

import guru.springframework.spring_6_rest_api.model.BeerCSVRecord;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCsv(File file);
}
