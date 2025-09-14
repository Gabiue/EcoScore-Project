package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.RegistroDiario;
import com.ucbprojeto.ecoscore.repository.RegistroDiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroDiarioService {
    private final RegistroDiarioRepository repository;

    public RegistroDiarioService(RegistroDiarioRepository repository) {
        this.repository = repository;
    }

    //Listar todos
    public List<RegistroDiario> listarTodos() {
        return repository.findAll();
    }
    //Buscar por ID
    public RegistroDiario listarPorId(int id) {
        return repository.findById(id);
    }
    //Listar por membro
    public List<RegistroDiario> listarPorMembro(String cpf_membro) {
        return repository.findByCpf(cpf_membro);
    }

    //CRUD
    public void salvar(RegistroDiario registroDiario) {
        repository.save(registroDiario);
    }
    public void atualizar(RegistroDiario registroDiario){
        repository.update(registroDiario);
    }
    public void deletar (int id){
        repository.delete(id);
    }



}
