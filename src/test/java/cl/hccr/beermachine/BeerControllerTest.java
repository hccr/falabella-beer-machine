package cl.hccr.beermachine;

import cl.hccr.beermachine.controller.BeerController;
import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.service.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerService beerService;

    @Test
    void getBeerItem_ShouldReturnBearItem() throws Exception{
        given(beerService.getBeerItem(1)).willReturn(new BeerItem(1,"Golden","Kross","Chile",10.5,"EUR"));


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
}
