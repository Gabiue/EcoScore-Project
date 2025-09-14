package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.PraticaSustentavel;
import com.ucbprojeto.ecoscore.repository.PraticaSustentavelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PraticaSustentavelService {
    private final PraticaSustentavelRepository repository;

    public PraticaSustentavelService(PraticaSustentavelRepository repository) {
        this.repository = repository;
    }

    //Listar todos
    public List<PraticaSustentavel> ListarTodos() {
        return repository.findAll();
    }
    public PraticaSustentavel ListarPorId(int id) {
        return repository.findById(id);
    }
    public List<PraticaSustentavel> ListarPorCategoria(int categoria) {
        return repository.findByCategory(categoria);
    }
    public List<PraticaSustentavel> ListarPorDificuldade(PraticaSustentavel.EDificuldade dificuldade) {
        return repository.findByDificuldade(dificuldade);
    }

    public void salvar(PraticaSustentavel praticaSustentavel) {
        repository.save(praticaSustentavel);
    }
    public void atualizar(PraticaSustentavel praticaSustentavel){
        repository.update(praticaSustentavel);
    }
    public void deletar (int id){
        repository.delete(id);
    }

}
