package service;

public class TimeConflictException extends RuntimeException {

    public TimeConflictException(String message) {
        super(message);
    }
}