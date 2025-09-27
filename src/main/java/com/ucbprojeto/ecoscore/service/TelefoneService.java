package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Telefone;
import com.ucbprojeto.ecoscore.repository.TelefoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefoneService {
    private final TelefoneRepository repository;

    public TelefoneService(TelefoneRepository repository) {
        this.repository = repository;
    }

    public List<Telefone> listarTodos() {
        return repository.findAll();
    }
    public List<Telefone> buscarPorCpf(String cpf) {
        return repository.findByMembroCpf(cpf);
    }
    public void salvar(Telefone telefone) {
        repository.save(telefone);
    }
    public void atualizar(Telefone telefone) {
        repository.update(telefone);
    }
    public void deletar(int id_telefone) {
        repository.delete(id_telefone);
    }

}
