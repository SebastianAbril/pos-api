package com.sebastianabril.pos.api.controller;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebastianabril.pos.api.controller.dto.UserDTO;
import com.sebastianabril.pos.api.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void saveTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Cleo");
        userDTO.setLastName("Abril");
        userDTO.setEmail("cleoAbril@gmail.com");
        userDTO.setPassword("cleo123567");
        userDTO.setRoleId(1);

        mockMvc
            .perform(
                post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO))
            )
            .andExpect(status().isCreated());
    }

    @Test
    public void saveInvalidEmailTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Cleo");
        userDTO.setLastName("Abril");
        userDTO.setEmail("correoInvalido");
        userDTO.setPassword("cleo123aaaaaa");
        userDTO.setRoleId(1);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("MethodArgumentNotValidException")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fields[0].message", Matchers.is("Insert a valid address")));
    }

    @Test
    public void saveInvalidNameTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("");
        userDTO.setLastName("Abril");
        userDTO.setEmail("Cleo@gmail.com");
        userDTO.setPassword("cleo123aaaaaa");
        userDTO.setRoleId(1);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("MethodArgumentNotValidException")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fields[0].message", Matchers.is("Insert a name")));
    }
}
