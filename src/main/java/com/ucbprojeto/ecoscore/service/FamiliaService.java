package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Familia;
import com.ucbprojeto.ecoscore.repository.FamiliaRepository;
import com.ucbprojeto.ecoscore.repository.MembroAdultoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamiliaService {
    private final FamiliaRepository repository;
    private final MembroAdultoRepository membroAdultoRepository;

    public FamiliaService(FamiliaRepository repository, MembroAdultoRepository membroAdultoRepository) {
        this.repository = repository;
        this.membroAdultoRepository = membroAdultoRepository;
    }

    //Listar todos as familias
    public List<Familia> listar() {
        return repository.findAll();

    }
    //Buscar familia por ID
    public Familia buscarPorId(int id) {
        return repository.findById(id);
    }
    //Salvar nova familia
    public void salvar(Familia familia){
        if(familia.getNome() == null || familia.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("O nome da família não pode ser nulo ou vazio.");
        }
        //Verificar se tem o CEP é valido e se o cep tem 8 digitos / 9 digitos com hifen
        if (familia.getCep() == null || !familia.getCep().matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("O CEP deve ser válido e conter 8 dígitos (com ou sem hífen).");
        }
        // Verificar se tem membro responsável
        if(!membroAdultoRepository.existeResponsavel(familia.getId_familia())){
            throw new IllegalArgumentException("A família deve ter pelo menos um membro responsável adulto.");
        }

        repository.save(familia);
    }
    //Atualizar familia existente
    public void atualizar(Familia familia){
        if(familia.getNome() == null || familia.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("O nome da família não pode ser nulo ou vazio.");
        }
        //Verificar se tem o CEP é valido e se o cep tem 8 digitos / 9 digitos com hifen
        if (familia.getCep() == null || !familia.getCep().matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("O CEP deve ser válido e conter 8 dígitos (com ou sem hífen).");
        }
        // Verificar se tem membro responsável
        if(!membroAdultoRepository.existeResponsavel(familia.getId_familia())){
            throw new IllegalArgumentException("A família deve ter pelo menos um membro responsável adulto.");
        }
        repository.update(familia);
    }
    //Deletar familia pelo ID
    public void deletar(int id){
        repository.delete(id);
    }

}
