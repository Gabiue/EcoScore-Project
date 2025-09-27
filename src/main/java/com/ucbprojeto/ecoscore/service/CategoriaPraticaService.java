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
        repository.save(categoriaPratica);
    }
    public void atualizar(CategoriaPratica categoriaPratica) {
        repository.update(categoriaPratica);
    }
    public void deletar(int id) {
        repository.delete(id);
    }

}
