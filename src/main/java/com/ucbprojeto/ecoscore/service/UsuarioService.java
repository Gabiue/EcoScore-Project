package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Usuario;
import com.ucbprojeto.ecoscore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // Listar todos os usuários
    public List<Usuario> listar() {
        return repository.findAll();
    }

    // Buscar usuário por ID
    public Usuario buscarPorId(int id) {
        return repository.findById(id);
    }

    // Salvar novo usuário
    public void salvar(Usuario usuario) {
        repository.save(usuario);
    }

    // Atualizar usuário existente
    public void atualizar(Usuario usuario) {
        repository.update(usuario);
    }

    // Deletar usuário pelo ID
    public void deletar(int id) {
        repository.delete(id);
    }
}