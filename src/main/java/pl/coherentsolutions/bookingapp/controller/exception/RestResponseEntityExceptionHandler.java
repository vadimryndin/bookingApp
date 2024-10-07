package pl.coherentsolutions.bookingapp.controller.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ControllerErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        String devMessage = "Validation failed for " + String.join(", ", errors);
        String userMessage = "Invalid input. Please check the input data for "  + String.join(", ", errors);

        ControllerErrorResponse errorResponse = new ControllerErrorResponse();
        errorResponse.setDevMessage(devMessage);
        errorResponse.setUserMessage(userMessage);
        errorResponse.setCode("VALIDATION_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ControllerErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        String devMessage = ex.getReason();
        String userMessage = "The requested resource was not found. " + ex.getReason();

        ControllerErrorResponse errorResponse = new ControllerErrorResponse();
        errorResponse.setDevMessage(devMessage);
        errorResponse.setUserMessage(userMessage);
        errorResponse.setCode("RESOURCE_NOT_FOUND");

        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }
}

@Setter
@Getter
@NoArgsConstructor
class ControllerErrorResponse {
    private String devMessage;
    private String userMessage;
    private String code;

    @Override
    public String toString() {
        return "ControllerErrorResponse{" +
                "devMessage='" + devMessage + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
