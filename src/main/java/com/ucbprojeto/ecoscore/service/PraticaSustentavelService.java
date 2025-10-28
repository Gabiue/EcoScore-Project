package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.PraticaSustentavel;
import com.ucbprojeto.ecoscore.repository.CategoriaPraticaRepository; // validar se a categoria existe.
import com.ucbprojeto.ecoscore.repository.PraticaSustentavelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PraticaSustentavelService {
    private final PraticaSustentavelRepository repository;
    private final CategoriaPraticaRepository categoriaRepository;

    public PraticaSustentavelService(PraticaSustentavelRepository repository,
                                     CategoriaPraticaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todos
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
        validar(praticaSustentavel);
        repository.save(praticaSustentavel);
    }

    public void atualizar(PraticaSustentavel praticaSustentavel) {
        validar(praticaSustentavel);
        repository.update(praticaSustentavel);
    }

    public void deletar(int id) {
        repository.delete(id);
    }

    private void validar(PraticaSustentavel pratica) {
        if (pratica == null) {
            throw new IllegalArgumentException("Prática sustentável não pode ser nula");
        }

        // Nome
        if (pratica.getNome() == null || pratica.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da prática é obrigatório");
        }

        if (pratica.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome da prática deve ter no mínimo 3 caracteres");
        }

        if (pratica.getNome().trim().length() > 150) {
            throw new IllegalArgumentException("Nome da prática não pode ter mais de 150 caracteres");
        }

        // Descrição
        if (pratica.getDescricao() != null && !pratica.getDescricao().trim().isEmpty()) {
            if (pratica.getDescricao().trim().length() > 500) {
                throw new IllegalArgumentException("Descrição não pode ter mais de 500 caracteres");
            }
        }

        // Pontos base
        if (pratica.getPontos_base() < 0) {
            throw new IllegalArgumentException("Pontos base não pode ser negativo");
        }

        if (pratica.getPontos_base() > 1000) {
            throw new IllegalArgumentException("Pontos base não pode ser maior que 1000");
        }

        // Dificuldade
        if (pratica.getDificuldade() == null) {
            throw new IllegalArgumentException("Dificuldade é obrigatória");
        }

        // Validar se é um valor válido do enum
        boolean dificuldadeValida = false;
        for (PraticaSustentavel.EDificuldade d : PraticaSustentavel.EDificuldade.values()) {
            if (d == pratica.getDificuldade()) {
                dificuldadeValida = true;
                break;
            }
        }
        if (!dificuldadeValida) {
            throw new IllegalArgumentException("Dificuldade inválida. Use: FACIL, MEDIA ou DIFICIL");
        }

        // Frequência esperada
        if (pratica.getFrequencia_esperada() != null && !pratica.getFrequencia_esperada().trim().isEmpty()) {
            if (pratica.getFrequencia_esperada().trim().length() > 50) {
                throw new IllegalArgumentException("Frequência esperada não pode ter mais de 50 caracteres");
            }

            // Validar valores comuns (opcional, mas recomendado)
            String freq = pratica.getFrequencia_esperada().trim().toUpperCase();
            if (!freq.equals("DIARIA") && !freq.equals("SEMANAL") &&
                    !freq.equals("MENSAL") && !freq.equals("EVENTUAL") &&
                    !freq.equals("DIÁRIO") && !freq.isEmpty()) {
                // Permite outros valores, mas avisa se não for um padrão
            }
        }

        // ID da categoria
        if (pratica.getId_categoria() <= 0) {
            throw new IllegalArgumentException("ID da categoria é obrigatório e deve ser maior que zero");
        }

        // Verifica se a categoria existe
        if (categoriaRepository.findById(pratica.getId_categoria()) == null) {
            throw new IllegalArgumentException("Categoria informada não existe");
        }
    }
}