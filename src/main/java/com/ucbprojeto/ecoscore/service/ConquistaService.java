package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Conquista;
import com.ucbprojeto.ecoscore.repository.ConquistaRepository;
import com.ucbprojeto.ecoscore.repository.MetaRepository; // Adicionei como dependência porque precisamos validar se a meta existe.
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ConquistaService {
    private final ConquistaRepository repository;
    private final MetaRepository metaRepository;

    public ConquistaService(ConquistaRepository repository, MetaRepository metaRepository) {
        this.repository = repository;
        this.metaRepository = metaRepository;
    }

    public List<Conquista> listarTodos() {
        return repository.findAll();
    }

    public Conquista buscarPorId(int id_conquista, int id_meta){
        return repository.findById(id_conquista, id_meta);
    }

    public void salvar(Conquista conquista) {
        validar(conquista);
        repository.save(conquista);
    }

    public void atualizar(Conquista conquista) {
        validar(conquista);
        repository.update(conquista);
    }

    public void deletar(int id_conquista, int id_meta) {
        repository.delete(id_conquista, id_meta);
    }

    private void validar(Conquista conquista) {
        if (conquista == null) {
            throw new IllegalArgumentException("Conquista não pode ser nula");
        }

        // ID da meta
        if (conquista.getId_meta() <= 0) {
            throw new IllegalArgumentException("ID da meta é obrigatório e deve ser maior que zero");
        }

        // Verifica se a meta existe
        if (metaRepository.findById(conquista.getId_meta()) == null) {
            throw new IllegalArgumentException("Meta informada não existe");
        }

        // Data da conquista
        if (conquista.getData_conquista() == null) {
            throw new IllegalArgumentException("Data da conquista é obrigatória");
        }

        // Verifica se a data não está no futuro
        Date dataAtual = new Date(System.currentTimeMillis());
        if (conquista.getData_conquista().after(dataAtual)) {
            throw new IllegalArgumentException("Data da conquista não pode ser no futuro");
        }

        // Pontos bônus
        if (conquista.getPontos_bonus() < 0) {
            throw new IllegalArgumentException("Pontos bônus não pode ser negativo");
        }

        if (conquista.getPontos_bonus() > 10000) {
            throw new IllegalArgumentException("Pontos bônus não pode ser maior que 10000");
        }
    }
}
