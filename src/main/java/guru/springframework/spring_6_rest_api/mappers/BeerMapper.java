package guru.springframework.spring_6_rest_api.mappers;

import org.mapstruct.Mapper;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.model.BeerDTO;

// Note: the config as provided on course does not work, this is how you got it to work:
// https://www.youtube.com/watch?app=desktop&v=7UC3ZjQnric
@Mapper(componentModel = "spring")
public interface BeerMapper {
    
    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBearDto(Beer beer);
}
