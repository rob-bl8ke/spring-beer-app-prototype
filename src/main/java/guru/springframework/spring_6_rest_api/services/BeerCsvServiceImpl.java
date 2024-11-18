package guru.springframework.spring_6_rest_api.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import guru.springframework.spring_6_rest_api.model.BeerCSVRecord;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

    @Override
    public List<BeerCSVRecord> convertCsv(File csvFile) {
        try {
            List<BeerCSVRecord> beerCsvRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                .withType(BeerCSVRecord.class)
                .build().parse();

            return beerCsvRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
