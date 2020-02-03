package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;

public interface BeerService {
    BeerItem getBeerItem(int id);

    void createBeerItem(NewBeerItemRequestDTO newBeerItemRequest);
}
