package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerBoxDTO;

public interface BeerBoxService {
    BeerBoxDTO getBoxPrice(int idCerveza, String currency, int quantity);
}
