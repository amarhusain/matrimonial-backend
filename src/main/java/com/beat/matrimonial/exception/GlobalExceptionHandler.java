package com.beat.matrimonial.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public final ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
      UserAlreadyExistsException ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("CONFLICT", ex.getMessage(), request.getDescription(false)),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("NOT_FOUND", ex.getMessage(), request.getDescription(false)),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex,
      WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("BAD_REQUEST", ex.getMessage(), request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(InvalidOtpException.class)
  public ResponseEntity<Object> handleInvalidOtpException(InvalidOtpException ex,
      WebRequest request) {
    String errorMessage = "Invalid OTP: " + ex.getMessage();
    return new ResponseEntity<>(
        generateErrorResponse("UNAUTHORIZED", errorMessage, request.getDescription(false)),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(SignupRequestNotFoundException.class)
  public ResponseEntity<Object> handleSignupRequestNotFoundException(
      SignupRequestNotFoundException ex, WebRequest request) {
    String errorMessage = "Signup request not found: " + ex.getMessage();
    return new ResponseEntity<>(
        generateErrorResponse("NOT_FOUND", errorMessage, request.getDescription(false)),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex,
      WebRequest request) {
    String errorMessage = "Role not found: " + ex.getMessage();
    return new ResponseEntity<>(
        generateErrorResponse("NOT_FOUND", errorMessage, request.getDescription(false)),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnderageException.class)
  public ResponseEntity<Object> handleUnderageExceptionException(UnderageException ex,
      WebRequest request) {
    String errorMessage = "Underage: " + ex.getMessage();
    return new ResponseEntity<>(
        generateErrorResponse("BAD_REQUEST", errorMessage, request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public final ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException ex,
      WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("UNAUTHORIZED", ex.getMessage(), request.getDescription(false)),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public final ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("BAD_REQUEST", ex.getMessage(),
            request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileUploadException.class)
  public final ResponseEntity<ErrorResponse> handleFileUploadException(
      FileUploadException ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("BAD_REQUEST", ex.getMessage(),
            request.getDescription(false)),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IOException.class)
  public final ResponseEntity<ErrorResponse> handleIOException(
      IOException ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage(),
            request.getDescription(false)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        generateErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage(),
            request.getDescription(false)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private ErrorResponse generateErrorResponse(String errorCode, String errorMessage,
      String errorDetail) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorCode(errorCode);
    errorResponse.setErrorMessage(errorMessage);
    errorResponse.setErrorDetail(errorDetail);
    errorResponse.setTimestamp(LocalDateTime.now());
    return errorResponse;
  }
}

