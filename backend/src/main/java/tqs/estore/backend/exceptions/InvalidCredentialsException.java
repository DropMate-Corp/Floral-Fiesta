package tqs.estore.backend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends Exception{
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(){
        super("Invalid login credentials.");
    }
}
