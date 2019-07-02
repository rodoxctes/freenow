package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The car you've selected is being used by another driver.")
public class CarAlreadyInUseException extends Exception
{

    private static final long serialVersionUID = -5037855402677128514L;

    public CarAlreadyInUseException(String message)
    {
        super(message);
    }

}
