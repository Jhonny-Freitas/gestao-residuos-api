package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.dto.request.*;
import com.fiap.gestao.residuos.dto.response.AuthResponse;
import com.fiap.gestao.residuos.exception.BusinessException;
import com.fiap.gestao.residuos.model.Usuario;
import com.fiap.gestao.residuos.repository.UsuarioRepository;
import com.fiap.gestao.residuos.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String token = tokenProvider.generateToken(authentication);
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return new AuthResponse(token, usuario.getEmail(), usuario.getNome(), usuario.getRole());
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole("ROLE_USER");
        usuario.setAtivo(true);

        Usuario saved = usuarioRepository.save(usuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponse(token, saved.getEmail(), saved.getNome(), saved.getRole());
    }
}
