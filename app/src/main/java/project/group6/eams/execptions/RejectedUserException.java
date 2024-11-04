package project.group6.eams.execptions;

public class RejectedUserException extends RuntimeException {
    public RejectedUserException () {}
    public RejectedUserException (String message) {
        super(message);
    }
}
