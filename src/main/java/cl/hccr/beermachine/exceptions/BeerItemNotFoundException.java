package cl.hccr.beermachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "El Id de la cerveza no existe")
public class BeerItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4004070872735658332L;
}
