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
import pl.coherentsolutions.bookingapp.mapper.OrderMapper;
import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;
import pl.coherentsolutions.bookingapp.service.apartment.ApartmentService;
import pl.coherentsolutions.bookingapp.service.booking.BookingService;
import pl.coherentsolutions.bookingapp.service.order.OrderService;
import pl.coherentsolutions.bookingapp.service.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/bookingApp/orders")
public class OrderController {
    private final OrderService orderService;
    private final BookingService bookingService;
    private final OrderMapper orderMapper;
    private final ApartmentService apartmentService;
    private final UserService userService;

    @Operation(summary = "Find order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderEntity.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public OrderDto findOrderById(@PathVariable Integer id) {
        Optional<OrderEntity> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return orderMapper.orderEntityToDto(order.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id " + id + " not found");
        }
    }

    @Operation(summary = "Find all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderEntity.class)) })
    })
    @GetMapping("/")
    @Secured({"ROLE_ADMIN"})
    public List<OrderDto> findAllOrders() {
        return orderService.getAllOrders().stream().map(orderMapper::orderEntityToDto).toList();
    }

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void createOrder(@Valid @RequestBody OrderDto orderDto) {
        BookingEntity booking = bookingService.findExistingBooking(orderDto);
        ApartmentEntity apartment = apartmentService.findExistingApartment(orderDto);

        OrderEntity order = orderMapper.orderDtoToEnity(orderDto);
        order.setBooking(booking);
        order.setApartment(apartment);

        orderService.saveOrUpdateOrder(order);
    }

    @Operation(summary = "Update an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderDto orderDto) {
        orderDto.setId(id);
        orderService.saveOrUpdateOrder(prepareOrderToUpdate(orderDto));
    }

    @Operation(summary = "Delete an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/cancel/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void deleteOrder(@PathVariable Integer id, Principal principal) {
        String username = principal.getName();
        userService.hasPermissionToCancelOrder(username, id);

        orderService.deleteOrder(id);
    }

    private OrderEntity prepareOrderToUpdate(OrderDto orderDto) {
        Optional<OrderEntity> existingOrderOptional = orderService.getOrderById(orderDto.getId());

        if (existingOrderOptional.isPresent()) {
            OrderEntity entityToUpdate = existingOrderOptional.get();
            if (orderDto.getOrderDate() != null) {
                entityToUpdate.setOrderDate(orderDto.getOrderDate());
            }
            if (orderDto.getApartmentId() != null) {
                entityToUpdate.setApartment(apartmentService.findExistingApartment(orderDto));
            }
            entityToUpdate.setTotalPrice(orderDto.getTotalPrice());

            return entityToUpdate;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order whit id " + orderDto.getId() + " does not exist");
        }
    }
}
