package ru.restaurant_voting.error;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}