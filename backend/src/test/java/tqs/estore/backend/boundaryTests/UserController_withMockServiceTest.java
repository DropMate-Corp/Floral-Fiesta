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
        user.setUserId(1L);
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
    public void whenRegisterValidUser_thenReturnUser_andStatus200() throws Exception {
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
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(user.getAddress()));


        verify(userService, times(1)).registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress());
    }

    @Test
    public void whenRegisterInvalidUser_thenReturnStatus400() throws Exception {
        // Valid User Registered
        when(userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress()))
                .thenReturn(user);

        User invalidUser = new User();
        invalidUser.setUserId(2L);
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

}