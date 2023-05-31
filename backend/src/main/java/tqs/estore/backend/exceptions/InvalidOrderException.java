package tqs.estore.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an invalid order is submitted.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOrderException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidOrderException(){
        super("Invalid order.");
    }

}
