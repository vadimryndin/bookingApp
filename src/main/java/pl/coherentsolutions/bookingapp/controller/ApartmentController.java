package pl.coherentsolutions.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.mapper.ApartmentMapper;
import pl.coherentsolutions.bookingapp.model.dto.ApartmentDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.service.apartment.ApartmentService;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/bookingApp/apartments")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @Operation(summary = "Find apartment by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the apartment",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApartmentEntity.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Apartment not found", content = @Content)
    })
    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ApartmentDto findApartmentById(@PathVariable Integer id) {
        Optional<ApartmentEntity> apartment = apartmentService.getApartmentById(id);
        if (apartment.isPresent()) {
            return apartmentMapper.apartmentEntityToDto(apartment.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Apartment with id " + id + " not found");
        }
    }

    @Operation(summary = "Find all apartments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the apartments",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApartmentEntity.class)) })
    })
    @GetMapping("/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<ApartmentDto> findAllApartments() {
        return apartmentService.getAllApartments().stream().map(apartmentMapper::apartmentEntityToDto).toList();
    }

    @Operation(summary = "Create a new apartment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Apartment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    @Secured({"ROLE_ADMIN"})
    public void createApartment(@Valid @RequestBody ApartmentDto apartmentDto) {
        apartmentService.saveOrUpdateApartment(apartmentMapper.apartmentDtoToEntity(apartmentDto));
    }

    @Operation(summary = "Update an existing apartment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apartment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Apartment not found")
    })
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void updateApartment(@PathVariable Integer id, @Valid @RequestBody ApartmentDto apartmentDto) {
        apartmentDto.setId(id);
        apartmentService.saveOrUpdateApartment(prepareApartmentToUpdate(apartmentDto));
    }

    @Operation(summary = "Delete a apartment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apartment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Apartment not found")
    })
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteApartment(@PathVariable Integer id) {
        apartmentService.deleteApartment(id);
    }

    private ApartmentEntity prepareApartmentToUpdate(ApartmentDto apartmentDto) {
        Optional<ApartmentEntity> existingApartmentOptional = apartmentService.getApartmentById(apartmentDto.getId());

        if (existingApartmentOptional.isPresent()) {
            ApartmentEntity entityToUpdate = existingApartmentOptional.get();
            if (apartmentDto.getName() != null) {
                entityToUpdate.setName(apartmentDto.getName());
            }
            if (apartmentDto.getAddress() != null) {
                entityToUpdate.setAddress(apartmentDto.getAddress());
            }
            entityToUpdate.setPrice(apartmentDto.getPrice());

            return entityToUpdate;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Apartment whit id " + apartmentDto.getId() + " does not exist");
        }
    }
}
