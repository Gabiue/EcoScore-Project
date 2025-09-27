package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Meta;
import com.ucbprojeto.ecoscore.repository.MetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaService {
    private final MetaRepository repository;

    public MetaService(MetaRepository repository) {
        this.repository = repository;
    }

    public List<Meta> listarTodos() {
        return repository.findAll();
    }
    public Meta buscarPorId(int id) {
        return repository.findById(id);
    }
    public List<Meta> buscarPorFamilia(int id_familia) {
        return repository.findByFamilyId(id_familia);
    }
    public List<Meta> listarPorStatus(Meta.EStatus status) {
        return repository.findByStatus(status);
    }
    public void salvar(Meta meta) {
        repository.save(meta);
    }
    public void atualizar(Meta meta) {
        repository.update(meta);
    }
    public void deletar(int id) {
        repository.delete(id);
    }

}
