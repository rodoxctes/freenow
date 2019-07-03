package com.mytaxi.controller;

import com.mytaxi.datatransferobject.UserDTO;
import com.mytaxi.domainobject.AuthenticationResponse;
import com.mytaxi.request.AuthenticationRequest;
import com.mytaxi.security.JwtTokenUtil;
import com.mytaxi.service.security.JwtUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * All operations with authentications will be routed by this controller.
 * <p/>
 */
@RestController
@CrossOrigin
@RequestMapping("v1/login")
@Api(
    value = "User Login",
    tags = {"User Login"}
)
public class LoginController
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    @ApiOperation(
        value = "Authenticate an Api User using JWT"
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    @ApiOperation(
        value = "Register an Api User in order to access with JWT"
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
