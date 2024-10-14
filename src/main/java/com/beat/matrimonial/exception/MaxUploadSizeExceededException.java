package com.beat.matrimonial.exception;

public class MaxUploadSizeExceededException extends RuntimeException {

  public MaxUploadSizeExceededException(String message) {
    super(message);
  }
}
