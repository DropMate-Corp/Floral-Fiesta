package tqs.estore.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.exceptions.DuplicatedEmailException;
import tqs.estore.backend.exceptions.InvalidCredentialsException;
import tqs.estore.backend.services.UserService;

@RestController
@RequestMapping("floralfiesta/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Integer phoneNumber, @RequestParam String address) throws DuplicatedEmailException {
        return new ResponseEntity<>(userService.registerUser(name, email, password, phoneNumber, address), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password) throws InvalidCredentialsException {
        return new ResponseEntity<>(userService.loginUser(email, password), HttpStatus.OK);
    }

}
