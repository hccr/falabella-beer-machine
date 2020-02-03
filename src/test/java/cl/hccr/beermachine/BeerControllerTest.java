package cl.hccr.beermachine;

import cl.hccr.beermachine.controller.BeerController;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
import cl.hccr.beermachine.exceptions.BeerItemNotFoundException;
import cl.hccr.beermachine.exceptions.IdAlreadyExistException;
import cl.hccr.beermachine.service.BeerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerService beerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBeerItem_ShouldReturnBearItem() throws Exception{
        given(beerService.getBeerItem(1)).willReturn(new BeerItemDTO(1,"Golden","Kross","Chile",10.5,"EUR"));

        mockMvc.perform(MockMvcRequestBuilders.get("/beers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("Id").value(1))
                .andExpect(jsonPath("Name").value("Golden"))
                .andExpect(jsonPath("Brewery").value("Kross"))
                .andExpect(jsonPath("Country").value("Chile"))
                .andExpect(jsonPath("Price").value(10.5))
                .andExpect(jsonPath("Currency").value("EUR"))
        ;
    }

    @Test
    void getBeerItem_NotFound() throws Exception{
        given(beerService.getBeerItem(0)).willThrow(new BeerItemNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/beers/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewBeerItem_ShouldReturnCreatedStatus()throws Exception{

        NewBeerItemRequestDTO newBeerItemRequest = new NewBeerItemRequestDTO(1,"Golden","Kross","Chile",10.5,"EUR");

        mockMvc.perform(MockMvcRequestBuilders.post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBeerItemRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    void createNewBeerItem_ShouldReturnBadRequestStatus()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createNewBeerItem_ShouldReturnConflictStatus()throws Exception{
        NewBeerItemRequestDTO newBeerItemRequest = new NewBeerItemRequestDTO(1,"Golden","Kross","Chile",10.5,"EUR");

        given(beerService.createBeerItem(any(NewBeerItemRequestDTO.class))).willThrow(new IdAlreadyExistException());


        mockMvc.perform(MockMvcRequestBuilders.post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBeerItemRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void getBeers_ShouldReturnBeersList()throws Exception{
        given(beerService.getBeers()).willReturn(Collections.singletonList(new BeerItemDTO(1,"Golden","Kross","Chile",10.5,"EUR")));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/beers"))
                .andExpect(status().isOk())
                .andReturn();

        List<BeerItemDTO> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BeerItemDTO>>() {});

        assertThat(response).isNotEmpty();
        assertThat(response.get(0).getId()).isEqualTo(1);
        assertThat(response.get(0).getName()).isEqualTo("Golden");
        assertThat(response.get(0).getBrewery()).isEqualTo("Kross");
        assertThat(response.get(0).getCountry()).isEqualTo("Chile");
        assertThat(response.get(0).getPrice()).isEqualTo(10.5);
        assertThat(response.get(0).getCurrency()).isEqualTo("EUR");

    }


}
