package cl.hccr.beermachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4004070872735658332L;
}
