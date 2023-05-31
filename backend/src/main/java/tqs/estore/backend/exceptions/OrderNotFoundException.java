package tqs.estore.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends Exception {

private static final long serialVersionUID = 1L;

    public OrderNotFoundException() {
        super("Order not found.");
    }
}
