package com.mytaxi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -5215599297231178399L;

    private String username;
    private String password;

}
