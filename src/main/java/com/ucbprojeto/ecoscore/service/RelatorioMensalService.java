package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.RelatorioMensal;
import com.ucbprojeto.ecoscore.repository.RelatorioMensalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioMensalService {
    private final RelatorioMensalRepository repository;

    public RelatorioMensalService(RelatorioMensalRepository repository) {
        this.repository = repository;
    }

    public List<RelatorioMensal> listarTodos(){
        return repository.findAll();
    }
    public RelatorioMensal buscarPorId(int id_relatorio){
        return repository.findById(id_relatorio);
    }
    public List<RelatorioMensal> buscarPorFamilia(int id_familia){
        return repository.findByFamilia(id_familia);
    }
    public void salvar(RelatorioMensal relatorioMensal){
        repository.save(relatorioMensal);
    }
    public void atualizar(RelatorioMensal relatorioMensal){
        repository.update(relatorioMensal);
    }
    public void deletar(int id_relatorio){
        repository.deleteById(id_relatorio);
    }

}
