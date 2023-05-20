package tqs.estore.backend.services;
import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.exceptions.DuplicatedEmailException;
import tqs.estore.backend.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password, Integer phoneNumber, String address) throws DuplicatedEmailException {
        return null;
    }

    public User loginUser(String email, String password){
        return null;
    }

}
