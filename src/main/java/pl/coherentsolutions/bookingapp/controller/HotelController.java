package pl.coherentsolutions.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.mapper.HotelMapper;
import pl.coherentsolutions.bookingapp.model.dto.HotelDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.service.hotel.HotelService;
import pl.coherentsolutions.bookingapp.service.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class HotelController {
    private HotelService hotelService;
    private HotelMapper hotelMapper;

    @Autowired
    private UserService userService;

    @Operation(summary = "Find hotel by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the hotel",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApartmentEntity.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content)
    })
    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public HotelDto findHotelById(@PathVariable Integer id) {
        Optional<HotelEntity> hotel = hotelService.getHotelById(id);
        if (hotel.isPresent()) {
            return hotelMapper.hotelEntityToDto(hotel.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel with id " + id + " not found");
        }
    }

    @Operation(summary = "Find all hotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found hotels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApartmentEntity.class)) })
    })
    @GetMapping("/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<HotelDto> findAllHotels() {
        return hotelService.getAllHotels().stream().map(hotelMapper::hotelEntityToDto).toList();
    }

    @Operation(summary = "Create a new hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    @Secured({"ROLE_ADMIN"})
    public void createHotel(@Valid @RequestBody HotelDto hotelDto) {
        hotelService.saveOrUpdateHotel(hotelMapper.hotelDtoToEntity(hotelDto));
    }

    @Operation(summary = "Update an existing hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @PutMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void updateHotel(@PathVariable Integer id, @Valid @RequestBody HotelDto hotelDto, Principal principal) {
        String username = principal.getName();
        userService.hasPermissionToEditHotel(username, hotelMapper.hotelDtoToEntity(hotelDto));

        hotelDto.setId(id);
        hotelService.saveOrUpdateHotel(prepareHotelToUpdate(hotelDto));
    }

    @Operation(summary = "Delete a hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteHotel(@PathVariable Integer id) {
        hotelService.deleteHotel(id);
    }

    private HotelEntity prepareHotelToUpdate(HotelDto hotelDto) {
        Optional<HotelEntity> existingApartmentOptional = hotelService.getHotelById(hotelDto.getId());

        if (existingApartmentOptional.isPresent()) {
            HotelEntity entityToUpdate = existingApartmentOptional.get();
            if (hotelDto.getName() != null) {
                entityToUpdate.setName(hotelDto.getName());
            }
            if (hotelDto.getAddress() != null) {
                entityToUpdate.setAddress(hotelDto.getAddress());
            }
            entityToUpdate.setStarsCount(hotelDto.getStarsCount());

            return entityToUpdate;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel whit id " + hotelDto.getId() + " does not exist");
        }
    }
}
