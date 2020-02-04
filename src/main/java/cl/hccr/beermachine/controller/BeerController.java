package cl.hccr.beermachine.controller;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.service.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BeerController {

    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/beers")
    public List<BeerItemDTO> getBeers(){
        return beerService.getBeers();
    }

    @PostMapping("/beers" )
    @ResponseStatus(value = HttpStatus.CREATED,reason = "Cerveza creada")
    public void createBeerItem(@RequestBody BeerItemDTO newBeerItemRequestDTO){
        beerService.createBeerItem(newBeerItemRequestDTO);
    }


    @GetMapping("/beers/{id}")
    public BeerItemDTO getBeer(@PathVariable int id){
        return beerService.getBeerItem(id);
    }

    @GetMapping("/beers/{id}/boxprice")
    public BeerBoxDTO getBeerBoxPrice(@PathVariable int id, @RequestParam(required = false) String currency, @RequestParam(required = false, defaultValue = "6") int quantity ){
        return beerService.getBoxPrice(id, currency, quantity);
    }
}
