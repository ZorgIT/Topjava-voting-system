package com.github.zorgit.restaurantvotingsystem.error;

public class VoteChangeNotAllowedException extends AppException {
    public VoteChangeNotAllowedException() {
        super("Cannot change vote after 11:00 AM.");
    }
}
