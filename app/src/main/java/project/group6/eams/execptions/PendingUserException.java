package project.group6.eams.execptions;

public class PendingUserException extends RuntimeException {
    public PendingUserException(){}
    public PendingUserException(String message) {
        super(message);
    }
}
