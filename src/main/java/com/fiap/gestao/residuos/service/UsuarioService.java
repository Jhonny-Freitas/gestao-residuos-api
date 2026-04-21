package com.fiap.gestao.residuos.service;

import com.fiap.gestao.residuos.exception.BusinessException;
import com.fiap.gestao.residuos.exception.ResourceNotFoundException;
import com.fiap.gestao.residuos.model.Usuario;
import com.fiap.gestao.residuos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
    }

    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
    }

    @Transactional
    public Usuario create(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + usuario.getEmail());
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        Usuario existing = findById(id);
        
        if (!existing.getEmail().equals(usuario.getEmail()) && repository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + usuario.getEmail());
        }
        
        existing.setNome(usuario.getNome());
        existing.setEmail(usuario.getEmail());
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            existing.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        existing.setRole(usuario.getRole());
        existing.setAtivo(usuario.getAtivo());
        
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário", "id", id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public Usuario toggleAtivo(Long id) {
        Usuario usuario = findById(id);
        usuario.setAtivo(!usuario.getAtivo());
        return repository.save(usuario);
    }
}
