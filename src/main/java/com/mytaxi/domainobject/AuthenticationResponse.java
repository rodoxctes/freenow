package com.mytaxi.domainobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 6649733296294263996L;

    private String token;

}
