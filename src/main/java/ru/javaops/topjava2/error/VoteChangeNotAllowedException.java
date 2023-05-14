package ru.javaops.topjava2.error;

public class VoteChangeNotAllowedException extends RuntimeException {
    public VoteChangeNotAllowedException() {
        super("Cannot change vote after 11:00 AM.");
    }
}
