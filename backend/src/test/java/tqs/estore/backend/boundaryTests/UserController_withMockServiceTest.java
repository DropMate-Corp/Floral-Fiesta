package tqs.estore.backend.boundaryTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.controllers.UserController;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.exceptions.DuplicatedEmailException;
import tqs.estore.backend.exceptions.InvalidCredentialsException;
import tqs.estore.backend.services.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserController_withMockServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("user@email.com");
        user.setPassword("password");
        user.setPhoneNumber(123456789);
        user.setAddress("Address");
    }

    @AfterEach
    public void tearDown() {
        user = null;
    }


    @Test
    void whenRegisterValidUser_thenReturnUser_andStatus200() throws Exception {
        when(userService.registerUser(user.getName(), user.getEmail(), user.getPassword() ,user.getPhoneNumber(), user.getAddress()))
                .thenReturn(user);

        mockMvc.perform(
                post("/floralfiesta/user/register")
                        .param("name", user.getName())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("phoneNumber", user.getPhoneNumber().toString())
                        .param("address", user.getAddress()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(user.getAddress()));


        verify(userService, times(1)).registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress());
    }

    @Test
    void whenRegisterInvalidUser_thenReturnStatus409() throws Exception {
        // Valid User Registered
        when(userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress()))
                .thenReturn(user);

        User invalidUser = new User();
        invalidUser.setName("Second User");
        invalidUser.setEmail("user@email.com"); // with the same email from User in setup
        invalidUser.setPassword("secondPassword");
        invalidUser.setPhoneNumber(987654321);
        invalidUser.setAddress("Second Address");

        when(userService.registerUser(invalidUser.getName(), invalidUser.getEmail(), invalidUser.getPassword(), invalidUser.getPhoneNumber(), invalidUser.getAddress()))
                .thenThrow(new DuplicatedEmailException());

        mockMvc.perform(
                post("/floralfiesta/user/register")
                        .param("name", invalidUser.getName())
                        .param("email", invalidUser.getEmail())
                        .param("password", invalidUser.getPassword())
                        .param("phoneNumber", invalidUser.getPhoneNumber().toString())
                        .param("address", invalidUser.getAddress()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email address is already registered."));

        verify(userService, times(1)).registerUser(invalidUser.getName(), invalidUser.getEmail(), invalidUser.getPassword(), invalidUser.getPhoneNumber(), invalidUser.getAddress());
    }

    @Test
    void whenLoginValidUser_thenReturnUser_andStatus200() throws Exception {
        when(userService.loginUser(user.getEmail(), user.getPassword()))
                .thenReturn(user);

        mockMvc.perform(
                        post("/floralfiesta/user/login")
                                .param("email", user.getEmail())
                                .param("password", user.getPassword()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(user.getAddress()));

        verify(userService, times(1)).loginUser(user.getEmail(), user.getPassword());
    }

    @Test
    void whenLoginWithInvalidPassword_thenReturnStatus401() throws Exception {
        String wrongPassword = "wrongPassword";
        when(userService.loginUser(user.getEmail(), wrongPassword))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(
                        post("/floralfiesta/user/login")
                                .param("email", user.getEmail())
                                .param("password", wrongPassword).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid login credentials."));

        verify(userService, times(1)).loginUser(user.getEmail(), wrongPassword);
    }

    @Test
    void whenLoginWithInvalidEmail_thenReturnStatus401() throws Exception {
        String wrongEmail = "wrongEmail@mail.com";
        when(userService.loginUser(wrongEmail, user.getPassword()))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(
                        post("/floralfiesta/user/login")
                                .param("email", wrongEmail)
                                .param("password", user.getPassword()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid login credentials."));

        verify(userService, times(1)).loginUser(wrongEmail, user.getPassword());
    }

}