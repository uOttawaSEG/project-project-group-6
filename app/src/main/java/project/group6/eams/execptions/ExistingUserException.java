package project.group6.eams.execptions;

public class ExistingUserException extends RuntimeException {
    public ExistingUserException () {}
    public ExistingUserException (String message) {
        super(message);
    }
}
