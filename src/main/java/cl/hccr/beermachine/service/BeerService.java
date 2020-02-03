package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;

import java.util.List;

public interface BeerService {
    BeerItemDTO getBeerItem(int id);

    BeerItemDTO createBeerItem(NewBeerItemRequestDTO newBeerItemRequest);

    List<BeerItemDTO> getBeers();
}
