package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid driver status")
public class OfflineStatusException extends Exception
{

    private static final long serialVersionUID = -4202441892466750373L;


    public OfflineStatusException(String message)
    {
        super(message);
    }

}
