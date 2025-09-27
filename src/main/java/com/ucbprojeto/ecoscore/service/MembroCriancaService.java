package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.MembroCrianca;
import com.ucbprojeto.ecoscore.repository.MembroCriancaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroCriancaService {
    private final MembroCriancaRepository repository;

    public MembroCriancaService(MembroCriancaRepository repository) {
        this.repository = repository;
    }

    public List<MembroCrianca> listarTodos() {
        return repository.findAll();
    }
    public MembroCrianca buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
    public void salvar(MembroCrianca membroCrianca) {
        repository.save(membroCrianca);
    }
    public void atualizar (MembroCrianca membroCrianca) {
        repository.update(membroCrianca);
    }
    public void deletar(String cpf) {
        repository.delete(cpf);
    }
}
