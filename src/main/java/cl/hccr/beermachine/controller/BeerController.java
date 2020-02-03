package cl.hccr.beermachine.controller;

import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
import cl.hccr.beermachine.service.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BeerController {

    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }


    @PostMapping("/beers" )
    @ResponseStatus(HttpStatus.CREATED)
    public void createBeerItem(@RequestBody NewBeerItemRequestDTO newBeerItemRequestDTO){
        beerService.createBeerItem(newBeerItemRequestDTO);

    }


    @GetMapping("/beers/{id}")
    public BeerItem getBeer(@PathVariable int id){

        return beerService.getBeerItem(id);
    }


}
