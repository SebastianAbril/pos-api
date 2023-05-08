package com.sebastianabril.pos.api.service;

import static org.mockito.Mockito.*;

import com.sebastianabril.pos.api.entity.Role;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.repository.RoleRepository;
import com.sebastianabril.pos.api.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    public void testSaveUser() {
        //given
        Role adminRole = new Role(1, "Admin", "Admin Role");
        when(roleRepository.findById(1)).thenReturn(Optional.of(adminRole));
        when(userRepository.findByEmail("pepe@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("hola123456")).thenReturn("123456holi");

        //when
        userService.save("Pepe", "Valencia", "pepe@gmail.com", "hola123456", 1);

        //then
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository).save(userCaptor.capture());
        User userCaptorInformation = userCaptor.getValue();
        Assertions.assertEquals("Pepe", userCaptorInformation.getName());
        Assertions.assertEquals("Valencia", userCaptorInformation.getLastName());
        Assertions.assertEquals("pepe@gmail.com", userCaptorInformation.getEmail());
        Assertions.assertEquals(1, userCaptorInformation.getRole().getId());
        Assertions.assertEquals("123456holi", userCaptorInformation.getPassword());
    }

    @Test
    public void testRoleIdNotFound() {
        when(roleRepository.findById(9)).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                userService.save("Pepe", "Valencia", "pepe@gmail.com", "hola123456", 9);
            }
        );

        Assertions.assertEquals("The Role with id: 9 does not exist", thrown.getMessage());
    }

    @Test
    public void testEmailAlreadyExist() {
        Role adminRole = new Role(1, "Admin", "Admin Role");
        when(roleRepository.findById(1)).thenReturn(Optional.of(adminRole));

        User user = new User(1, "Jhon", "Doe", "jhon@gmail.com", "123456", adminRole);
        when(userRepository.findByEmail("jhon@gmail.com")).thenReturn(Optional.of(user));

        RuntimeException thrown = Assertions.assertThrows(
            RuntimeException.class,
            () -> {
                userService.save("Hola", "Gutierrez", "jhon@gmail.com", "hola123456", 1);
            }
        );

        Assertions.assertEquals(
            "The e-mail jhon@gmail.com is already registered, try another one",
            thrown.getMessage()
        );
    }
}
