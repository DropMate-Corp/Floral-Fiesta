package tqs.estore.backend.services;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> registerUser(String name, String email, Integer phoneNumber, String address){
        return null;
    }

    public ResponseEntity<User> loginUser(String email, String password){
        return null;
    }

}
