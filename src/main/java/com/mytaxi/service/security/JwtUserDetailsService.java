package com.mytaxi.service.security;

import com.mytaxi.dataaccessobject.UserRepository;
import com.mytaxi.datatransferobject.UserDTO;
import com.mytaxi.domainobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserDO user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            new ArrayList<>());
    }

    public UserDO save(UserDTO user) {
        UserDO newUser = new UserDO(user.getUsername(),bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }
}
