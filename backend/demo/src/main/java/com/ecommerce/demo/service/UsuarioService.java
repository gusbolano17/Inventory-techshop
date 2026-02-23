package com.ecommerce.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.Users;
import com.ecommerce.demo.repository.UsersRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsersRepository usersRepository;

    public Optional<Users> findByEmail(String email) throws Exception{
        return this.usersRepository.findByEmail(email);
    }

    public void registrarRefreshToken(Users usuario, String refreshToken) throws Exception{
        usuario.setRefreshToken(refreshToken);
        usersRepository.save(usuario);
    }
    
}
