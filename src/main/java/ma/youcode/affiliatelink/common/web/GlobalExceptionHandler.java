package ma.youcode.affiliatelink.common.web;

import ma.youcode.affiliatelink.common.exceptions.BusinessRuleException;
import ma.youcode.affiliatelink.common.exceptions.EmailAlreadyExistsException;
import ma.youcode.affiliatelink.common.exceptions.ResourcesNotFoundException;
import ma.youcode.affiliatelink.common.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourcesNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleException ex){
        return build(HttpStatus.CONFLICT, ex.getMessage(), List.of());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidation(ValidationException ex){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, List<String> details){
        ApiError body = new ApiError(
                status.value(), status.getReasonPhrase(), message, details, Instant.now());
        return ResponseEntity.status(status).body(body);
    }
}
