package tqs.estore.backend.services;
import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.exceptions.DuplicatedEmailException;
import tqs.estore.backend.exceptions.InvalidCredentialsException;
import tqs.estore.backend.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /** This method registers a new user in the floralfiesta database
     * @param name - name of the user to be registered
     * @param email - email of the user to be registered
     * @param password - password of the user to be registered
     * @param phoneNumber - phone number of the user to be registered
     * @param address - address of the user to be registered
     * @return User - the user created in the database
     * @throws DuplicatedEmailException - if the email of the user already exists in the database
     **/

    public User registerUser(String name, String email, String password, Integer phoneNumber, String address) throws DuplicatedEmailException {

        if(userRepository.findByEmail(email) != null){
            throw new DuplicatedEmailException();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);

        return userRepository.save(user);
    }


    /** This method logs in a user in the floralfiesta database
     * @param email - email of the user to be logged in
     * @param password - password of the user to be logged in
     * @return User - the user logged in
     * @throws InvalidCredentialsException - if the email or password of the user are incorrect
     **/

    public User loginUser(String email, String password) throws InvalidCredentialsException {
        User user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        throw new InvalidCredentialsException();
    }

}
