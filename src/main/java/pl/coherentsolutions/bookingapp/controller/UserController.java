package pl.coherentsolutions.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.annotation.Audit;
import pl.coherentsolutions.bookingapp.mapper.UserMapper;
import pl.coherentsolutions.bookingapp.model.dto.MostActiveBookingUser;
import pl.coherentsolutions.bookingapp.model.dto.UserDto;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.service.user.UserService;
import pl.coherentsolutions.bookingapp.service.weather.WeatherService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookingApp/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    private WeatherService weatherService;

    @Audit
    @Operation(summary = "Find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)) })
    })
    @GetMapping("/")
    public List<UserDto> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return users.stream().map(userMapper::userEntityToDto).toList();
    }

    @Audit
    @Operation(summary = "Find user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        if (this.userService.getUserById(id).isPresent()) {
            UserEntity userEntity = this.userService.getUserById(id).get();
            return userMapper.userEntityToDto(userEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user whit id " + id + " not found");
        }
    }

    @Audit
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    public void createUser(@Valid @RequestBody UserDto user) {
        this.userService.saveOrUpdateUser(userMapper.userDtoToEntity(user));
    }

    @Audit
    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @RequestBody UserDto user) {
        user.setId(id);
        this.userService.saveOrUpdateUser(prepareUserToUpdate(user));
    }

    @Audit
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        this.userService.deleteUser(id);
    }

    @Audit
    @GetMapping("/mostActiveUser/{apartmentId}")
    public MostActiveBookingUser getMostActiveUser(@PathVariable Integer apartmentId, @RequestParam int months) {
        return userService.getMostActiveUserForApartment(apartmentId, months);
    }

    @Audit
    @GetMapping("/weather/{userId}")
    public ResponseEntity<String> getWeather(@PathVariable Integer userId) {
        Optional<UserEntity> userEntityOptional = userService.getUserById(userId);

        if (userEntityOptional.isPresent()) {
            String weather = weatherService.showWeatherByCity(userEntityOptional.get().getCity());

            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The user whit id " + userId + " not found");
        }
    }

    private UserEntity prepareUserToUpdate(UserDto user) {
        Optional<UserEntity> entityOptional = this.userService.getUserById(user.getId());

        if (entityOptional.isPresent()) {
            UserEntity entityToUpdate = entityOptional.get();
            if (user.getFirstName() != null) {
                entityToUpdate.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                entityToUpdate.setLastName(user.getLastName());
            }
            if (user.getUsername() != null) {
                entityToUpdate.setUsername(user.getUsername());
            }
            if (user.getEmail() != null) {
                entityToUpdate.setEmail(user.getEmail());
            }
            if (user.getPhoneNumber() != null) {
                entityToUpdate.setPhoneNumber(user.getPhoneNumber());
            }

            return entityToUpdate;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user whit id " + user.getId() + " does not exist");
        }
    }
}
