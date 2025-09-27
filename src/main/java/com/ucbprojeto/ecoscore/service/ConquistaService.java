package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Conquista;
import com.ucbprojeto.ecoscore.repository.ConquistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConquistaService {
    private final ConquistaRepository repository;

    public ConquistaService(ConquistaRepository repository) {
        this.repository = repository;
    }

    public List<Conquista> listarTodos() {
        return repository.findAll();
    }
    public Conquista buscarPorId(int id_conquista, int id_meta){
        return repository.findById(id_conquista, id_meta);
    }
    public void salvar(Conquista conquista) {
        repository.save(conquista);
    }
    public void atualizar(Conquista conquista) {
        repository.update(conquista);
    }
    public void deletar(int id_conquista, int id_meta) {
        repository.delete(id_conquista, id_meta);
    }
}
