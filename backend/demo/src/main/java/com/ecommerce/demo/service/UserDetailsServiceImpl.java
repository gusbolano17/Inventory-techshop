package com.ecommerce.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.Users;
import com.ecommerce.demo.repository.UsersRepository;
import com.ecommerce.demo.utils.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username).orElseThrow(
            () -> new UsernameNotFoundException("Usuario no encontrado")
        );

        return new UserDetailsImpl(users);
    }

    
}
