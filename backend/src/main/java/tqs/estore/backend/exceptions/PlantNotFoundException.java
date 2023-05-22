package tqs.estore.backend.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user tries to access a plant with an invalid id.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlantNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlantNotFoundException(){
        super("Plant was not found with provided id.");
    }
}
