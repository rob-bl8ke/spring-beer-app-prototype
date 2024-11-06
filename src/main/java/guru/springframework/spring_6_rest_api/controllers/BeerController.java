package guru.springframework.spring_6_rest_api.controllers;

import guru.springframework.spring_6_rest_api.model.Beer;
import guru.springframework.spring_6_rest_api.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        log.debug("Get beer by id was called in controller");
        return beerService.getBeerById(id);
    }
}
