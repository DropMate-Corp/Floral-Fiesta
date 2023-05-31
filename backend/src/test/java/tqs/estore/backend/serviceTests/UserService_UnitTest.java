package tqs.estore.backend.serviceTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.exceptions.DuplicatedEmailException;
import tqs.estore.backend.exceptions.InvalidCredentialsException;
import tqs.estore.backend.repositories.UserRepository;
import tqs.estore.backend.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserService_UnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setName("User");
        user.setEmail("user@email.com");
        user.setPassword("password");
        user.setPhoneNumber(123456789);
        user.setAddress("Address");
    }

    @AfterEach
    public void tearDown(){
        user = null;
    }

    @Test
    void whenRegisterValidUser_thenReturnUser () throws DuplicatedEmailException {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        User savedUser = userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress());

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(savedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(savedUser.getAddress()).isEqualTo(user.getAddress());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void whenRegisterInvalidUser_thenThrowDuplicatedEmailException () {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        User user2 = new User();
        user2.setName("User2");
        user2.setEmail(user.getEmail());
        user2.setPassword("password2");
        user2.setPhoneNumber(987654321);
        user2.setAddress("Address2");

        assertThatThrownBy(() -> userService.registerUser(user2.getName(), user2.getEmail(), user2.getPassword(), user2.getPhoneNumber(), user2.getAddress()))
                .isInstanceOf(DuplicatedEmailException.class)
                .hasMessageContaining("Email address is already registered.");

        verify(userRepository, times(1)).findByEmail(user2.getEmail());
        verify(userRepository, times(0)).save(user2);
    }

    @Test
    void whenLoginValidUser_thenReturnUser () throws InvalidCredentialsException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        User loggedUser = userService.loginUser(user.getEmail(), user.getPassword());

        assertThat(loggedUser).isNotNull();
        assertThat(loggedUser.getName()).isEqualTo(user.getName());
        assertThat(loggedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(loggedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(loggedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(loggedUser.getAddress()).isEqualTo(user.getAddress());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void whenLoginWithInvalidEmail_thenThrowInvalidCredentialsException () {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        assertThatThrownBy(() -> userService.loginUser(user.getEmail(), user.getPassword()))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid login credentials.");

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void whenLoginWithInvalidPassword_thenThrowInvalidCredentialsException () {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        assertThatThrownBy(() -> userService.loginUser(user.getEmail(), "invalidPassword"))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid login credentials.");

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

}
