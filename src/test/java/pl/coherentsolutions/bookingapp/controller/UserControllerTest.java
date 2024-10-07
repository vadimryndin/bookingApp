package pl.coherentsolutions.bookingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.coherentsolutions.bookingapp.model.UserRole;
import pl.coherentsolutions.bookingapp.model.dto.UserDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private Map<String, UserEntity> existingUserNameToUserDto;

    private final UserEntity johndoe = new UserEntity("John", "Doe", "johndoe", "password", "jdoe@ex.com", "+48911", UserRole.ROLE_USER, "Wroclaw");
    private final UserEntity billjames = new UserEntity("Bill", "James", "billjames", "password", "bj@ex.com", "+48112", UserRole.ROLE_USER, "Wroclaw");
    private final UserDto testUser = new UserDto("Bob", "Black", "bblack", "password", "bb@ex.com", "+48999", "Wroclaw");
    private final UserDto updatedTestUser = new UserDto("Ivan", "Ivanov", "johndoe", "password", "jdoe@ex.com", "+48911", "Wroclaw");


    @BeforeEach
    public void setup() {
        this.objectMapper = new ObjectMapper();

        userRepository.deleteAll();
        userRepository.saveAll(Arrays.asList(johndoe, billjames));

        existingUserNameToUserDto = ((List<UserEntity>) userRepository.findAll()).stream()
                .collect(Collectors.toMap(UserEntity::getUsername, userEntity -> userEntity));
    }

    @Test
    public void createUser_ShouldReturnOk() throws Exception {
        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void createUser_ShouldCreateUserInDatabase() throws Exception {
        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)));

        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        assertEquals(users.size(), existingUserNameToUserDto.size() + 1);

        UserEntity createdUser = users.stream()
                .filter(user -> "bblack".equals(user.getUsername()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdUser);
        assertEquals(createdUser.getFirstName(), testUser.getFirstName());
        assertEquals(createdUser.getLastName(), testUser.getLastName());
        assertEquals(createdUser.getUsername(), testUser.getUsername());
        assertEquals(createdUser.getEmail(), testUser.getEmail());
        assertEquals(createdUser.getPhoneNumber(), testUser.getPhoneNumber());
    }

    @Test
    public void getUserById_ShouldReturnUser() throws Exception {
        UserEntity johndoeEntity = existingUserNameToUserDto.get(johndoe.getUsername());
        assertNotNull(johndoeEntity);

        mockMvc.perform(get("/bookingApp/users/" + johndoeEntity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(johndoeEntity.getId()))
                .andExpect(jsonPath("$.firstName").value(johndoeEntity.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(johndoeEntity.getLastName()))
                .andExpect(jsonPath("$.username").value(johndoeEntity.getUsername()))
                .andExpect(jsonPath("$.email").value(johndoeEntity.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(johndoeEntity.getPhoneNumber()));
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        UserEntity johndoeEntity = existingUserNameToUserDto.get(johndoe.getUsername());
        UserEntity billjamesEntity = existingUserNameToUserDto.get(billjames.getUsername());
        assertNotNull(johndoeEntity);
        assertNotNull(billjamesEntity);

        mockMvc.perform(get("/bookingApp/users/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(existingUserNameToUserDto.size()))
                .andExpect(jsonPath("$[0].id").value(johndoeEntity.getId()))
                .andExpect(jsonPath("$[0].firstName").value(johndoeEntity.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(johndoeEntity.getLastName()))
                .andExpect(jsonPath("$[0].username").value(johndoeEntity.getUsername()))
                .andExpect(jsonPath("$[0].email").value(johndoeEntity.getEmail()))
                .andExpect(jsonPath("$[0].phoneNumber").value(johndoeEntity.getPhoneNumber()))
                .andExpect(jsonPath("$[1].id").value(billjamesEntity.getId()))
                .andExpect(jsonPath("$[1].firstName").value(billjamesEntity.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(billjamesEntity.getLastName()))
                .andExpect(jsonPath("$[1].username").value(billjamesEntity.getUsername()))
                .andExpect(jsonPath("$[1].email").value(billjamesEntity.getEmail()))
                .andExpect(jsonPath("$[1].phoneNumber").value(billjamesEntity.getPhoneNumber()));
    }

    @Test
    public void updateUser_ShouldReturnOk() throws Exception {
        UserEntity johndoeEntity = existingUserNameToUserDto.get(johndoe.getUsername());
        assertNotNull(johndoeEntity);

        mockMvc.perform(put("/bookingApp/users/" + johndoeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTestUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserFirstLastName_ShouldUpdateUserInDatabase() throws Exception {
        UserEntity johndoeEntity = existingUserNameToUserDto.get(johndoe.getUsername());
        assertNotNull(johndoeEntity);

        mockMvc.perform(put("/bookingApp/users/" + johndoeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTestUser)));

        UserEntity updatedUser = userRepository.findById(johndoeEntity.getId()).orElse(null);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getFirstName(), updatedTestUser.getFirstName());
        assertEquals(updatedUser.getLastName(), updatedTestUser.getLastName());
        assertEquals(updatedUser.getUsername(), updatedTestUser.getUsername());
        assertEquals(updatedUser.getEmail(), updatedTestUser.getEmail());
        assertEquals(updatedUser.getPhoneNumber(), updatedTestUser.getPhoneNumber());
    }

    @Test
    public void deleteUser_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/bookingApp/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_ShouldDeleteUserFromDatabase() throws Exception {
        mockMvc.perform(delete("/bookingApp/users/1")
                .accept(MediaType.APPLICATION_JSON));

        boolean isUserExists = userRepository.existsById(1);
        assertFalse(isUserExists);
    }

    @Test
    public void createUser_EmptyFirstName_ShouldReturnBadRequest() throws Exception {
        UserDto user = new UserDto("", "Doe", "johndoe", "password", "jdo@eex.com", "+48911", "Wroclaw");

        String expectedDevMessage = "Validation failed for firstName: First name should not be empty";
        String expectedUserMessage = "Invalid input. Please check the input data for firstName: First name should not be empty";

        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.devMessage").value(expectedDevMessage))
                .andExpect(jsonPath("$.userMessage").value(expectedUserMessage))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    public void createUser_EmptyLastName_ShouldReturnBadRequest() throws Exception {
        UserDto user = new UserDto("John", "", "johndoe", "password", "jdo@eex.com", "+48911", "Wroclaw");

        String expectedDevMessage = "Validation failed for lastName: Last name should not be empty";
        String expectedUserMessage = "Invalid input. Please check the input data for lastName: Last name should not be empty";

        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.devMessage").value(expectedDevMessage))
                .andExpect(jsonPath("$.userMessage").value(expectedUserMessage))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    public void createUser_EmptyUserName_ShouldReturnBadRequest() throws Exception {
        UserDto user = new UserDto("John", "Doe", "", "password", "jdoe@ex.com", "+48911", "Wroclaw");

        String expectedDevMessage = "Validation failed for username: Username should not be empty";
        String expectedUserMessage = "Invalid input. Please check the input data for username: Username should not be empty";

        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.devMessage").value(expectedDevMessage))
                .andExpect(jsonPath("$.userMessage").value(expectedUserMessage))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    public void createUser_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        UserDto user = new UserDto("John", "Doe", "johndoe", "password", "jdoeex.com", "+48911", "Wroclaw");

        String expectedDevMessage = "Validation failed for email: Email should be valid";
        String expectedUserMessage = "Invalid input. Please check the input data for email: Email should be valid";

        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.devMessage").value(expectedDevMessage))
                .andExpect(jsonPath("$.userMessage").value(expectedUserMessage))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    public void createUser_InvalidPhone_ShouldReturnBadRequest() throws Exception {
        UserDto user = new UserDto("John", "Doe", "johndoe", "password", "jdoe@ex.com", "phone", "Wroclaw");

        String expectedDevMessage = "Validation failed for phoneNumber: Phone number should be valid";
        String expectedUserMessage = "Invalid input. Please check the input data for phoneNumber: Phone number should be valid";

        mockMvc.perform(post("/bookingApp/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.devMessage").value(expectedDevMessage))
                .andExpect(jsonPath("$.userMessage").value(expectedUserMessage))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
