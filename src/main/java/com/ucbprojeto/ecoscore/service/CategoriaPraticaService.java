package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.CategoriaPratica;
import com.ucbprojeto.ecoscore.repository.CategoriaPraticaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaPraticaService {
    private final CategoriaPraticaRepository repository;

    public CategoriaPraticaService(CategoriaPraticaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaPratica> listarTodos() {
        return repository.findAll();
    }

    public CategoriaPratica buscarPorId(int id) {
        return repository.findById(id);
    }

    public void salvar(CategoriaPratica categoriaPratica) {
        validar(categoriaPratica);
        repository.save(categoriaPratica);
    }

    public void atualizar(CategoriaPratica categoriaPratica) {
        validar(categoriaPratica);
        repository.update(categoriaPratica);
    }

    public void deletar(int id) {
        repository.delete(id);
    }

    private void validar(CategoriaPratica categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }

        // Nome
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }

        if (categoria.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome da categoria deve ter no mínimo 3 caracteres");
        }

        if (categoria.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome da categoria não pode ter mais de 100 caracteres");
        }

        // Fator multiplicador
        if (categoria.getFator_multiplicador() <= 0) {
            throw new IllegalArgumentException("Fator multiplicador deve ser maior que zero");
        }

        if (categoria.getFator_multiplicador() > 10) {
            throw new IllegalArgumentException("Fator multiplicador não pode ser maior que 10");
        }
    }
}
