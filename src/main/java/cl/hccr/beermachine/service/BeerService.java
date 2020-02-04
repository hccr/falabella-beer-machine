package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItemDTO;

import java.util.List;

public interface BeerService {
    BeerItemDTO getBeerItem(int id);

    BeerItemDTO createBeerItem(BeerItemDTO newBeerItemRequest);

    List<BeerItemDTO> getBeers();

    BeerBoxDTO getBoxPrice(int idCerveza, String currency, int quantity);
}
