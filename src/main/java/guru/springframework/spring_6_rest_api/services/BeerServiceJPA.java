package guru.springframework.spring_6_rest_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring_6_rest_api.entities.Beer;
import guru.springframework.spring_6_rest_api.mappers.BeerMapper;
import guru.springframework.spring_6_rest_api.model.BeerDTO;
import guru.springframework.spring_6_rest_api.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
            .stream()
            .map(beerMapper::beerToBeerDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(
            beerRepository.findById(id)
            .orElse(null)
        ));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        Beer savedBeer =  beerRepository.save(beerMapper.beerDtoToBeer(beer));
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beer) {
        beerRepository.findById(beerId).ifPresent(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setUpdateDate(LocalDateTime.now());

            beerRepository.save(foundBeer);
        });
    }

    @Override
    public void deleteById(UUID beerId) {

    }

    @Override
    public void patchById(UUID beerId, BeerDTO beer) {

    }

}