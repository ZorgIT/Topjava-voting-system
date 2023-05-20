package com.github.zorgit.restaurantvotingsystem.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg);
    }
}