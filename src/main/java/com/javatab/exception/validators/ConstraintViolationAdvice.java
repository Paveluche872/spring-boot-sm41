package com.javatab.exception.validators;

public class ConstraintViolationAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorMessage> resourceNotFoundException(ConstraintViolationException ex, WebRequest request) {
        ValidationErrorMessage error = new ValidationErrorMessage();
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        ApiErrorMessage message = ApiErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .causes(error.getViolations())
                .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorMessage> onMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ValidationErrorMessage error = new ValidationErrorMessage();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        ApiErrorMessage message = ApiErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .causes(error.getViolations())
                .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
