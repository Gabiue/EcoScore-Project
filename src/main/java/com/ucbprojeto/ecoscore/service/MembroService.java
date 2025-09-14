package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Membro;
import com.ucbprojeto.ecoscore.repository.MembroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembroService {
    private final MembroRepository repository;

    public MembroService(MembroRepository repository) {
        this.repository = repository;
    }

    //LISTAR TODOS
    public List<Membro> listar() {
        return repository.findAll();
    }
    public List<Membro> listarPorFamilia(int id_familia) {
        return repository.findByFamily(id_familia);
    }
    public Membro buscarPorId(String cpf) {
        return repository.findById(cpf);
    }
    public void salvar(Membro membro) {
        repository.save(membro);
    }
    public void atualizar(Membro membro) {
        repository.update(membro);
    }
    public void deletar(String cpf) {
        repository.delete(cpf);
    }
}
