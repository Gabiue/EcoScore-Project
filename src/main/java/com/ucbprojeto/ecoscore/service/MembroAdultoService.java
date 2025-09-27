package com.ucbprojeto.ecoscore.service;


import com.ucbprojeto.ecoscore.model.MembroAdulto;
import com.ucbprojeto.ecoscore.repository.MembroAdultoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroAdultoService {
    private final MembroAdultoRepository repository;

    public MembroAdultoService(MembroAdultoRepository repository) {
        this.repository = repository;
    }

    public List<MembroAdulto> listarTodos() {
        return repository.findAll();
    }
    public MembroAdulto buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
    public void salvar(MembroAdulto membroAdulto) {
        repository.save(membroAdulto);
    }
    public void atualizar(MembroAdulto membroAdulto) {
        repository.update(membroAdulto);
    }
    public void deletar(String cpf) {
        repository.deleteBycpf(cpf);
    }
}

