package tqs.estore.backend.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user tries to register with an email that is already registered.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicatedEmailException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicatedEmailException(){
        super("Email address is already registered.");
    }
}
