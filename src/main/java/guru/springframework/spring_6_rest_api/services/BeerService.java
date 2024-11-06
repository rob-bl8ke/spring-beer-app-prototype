package guru.springframework.spring_6_rest_api.services;

import guru.springframework.spring_6_rest_api.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
