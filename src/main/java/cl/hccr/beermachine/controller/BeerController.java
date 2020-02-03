package cl.hccr.beermachine.controller;

import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
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
    public void createBeerItem(@RequestBody NewBeerItemRequestDTO newBeerItemRequestDTO){
        beerService.createBeerItem(newBeerItemRequestDTO);

    }


    @GetMapping("/beers/{id}")
    public BeerItemDTO getBeer(@PathVariable int id){

        return beerService.getBeerItem(id);
    }


}
