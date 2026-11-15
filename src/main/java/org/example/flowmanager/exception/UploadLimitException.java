package org.example.flowmanager.exception;

public class UploadLimitException extends RuntimeException {
    public UploadLimitException(String message) {
        super(message);
    }
}
