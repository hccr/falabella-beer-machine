package cl.hccr.beermachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IdAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 7003581299800530764L;
}
