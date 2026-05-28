package br.com.hadryan.coupon.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse<Void>> handleResourceNotFoundException(
            CouponNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCouponCodeException.class)
    public ResponseEntity<ErrorResponse<Void>> handleInvalidCouponCodeException(InvalidCouponCodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.badRequest()
                .body(ErrorResponse.error("Erro de validação", errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse<Void>> handleBusinessException(BusinessException ex) {
        Map<String, String> errors = ex.getResponse();
        var errorCode = errors.get("error_code");
        var errorMessage = errors.get("error_message");

        var response = String.format("%s: %s", errorCode, errorMessage);

        return ResponseEntity.status(ex.getStatusCode())
                .body(ErrorResponse.error(response));
    }

}
