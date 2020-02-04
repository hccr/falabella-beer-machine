package cl.hccr.beermachine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BeerBoxDTO {

    @JsonProperty("Price Total")
    private double priceTotal;

    public BeerBoxDTO() {
    }

    public BeerBoxDTO(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }
}
