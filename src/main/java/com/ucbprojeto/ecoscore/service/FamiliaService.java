package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Familia;
import com.ucbprojeto.ecoscore.repository.FamiliaRepository;
import com.ucbprojeto.ecoscore.repository.MembroAdultoRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class FamiliaService {
    private final FamiliaRepository repository;
    private final MembroAdultoRepository membroAdultoRepository;

    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    public FamiliaService(FamiliaRepository repository, MembroAdultoRepository membroAdultoRepository) {
        this.repository = repository;
        this.membroAdultoRepository = membroAdultoRepository;
    }

    // Listar todas as familias
    public List<Familia> listar() {
        return repository.findAll();
    }

    // Buscar familia por ID
    public Familia buscarPorId(int id) {
        return repository.findById(id);
    }

    // Salvar nova familia
    public void salvar(Familia familia) {
        validar(familia, false); // false = validação para criação (não valida responsável ainda)
        repository.save(familia);
    }

    // Atualizar familia existente
    public void atualizar(Familia familia) {
        validar(familia, true); // true = validação para atualização (valida responsável)
        repository.update(familia);
    }

    // Deletar familia pelo ID
    public void deletar(int id) {
        repository.delete(id);
    }

    private void validar(Familia familia, boolean validarResponsavel) {
        if (familia == null) {
            throw new IllegalArgumentException("Família não pode ser nula");
        }

        // Nome
        if (familia.getNome() == null || familia.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da família é obrigatório");
        }

        if (familia.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome da família deve ter no mínimo 3 caracteres");
        }

        if (familia.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome da família não pode ter mais de 100 caracteres");
        }

        // CEP
        if (familia.getCep() == null || familia.getCep().trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }

        if (!CEP_PATTERN.matcher(familia.getCep()).matches()) {
            throw new IllegalArgumentException("CEP inválido. Formato esperado: 00000-000 ou 00000000");
        }

        // Rua
        if (familia.getRua() != null && !familia.getRua().trim().isEmpty()) {
            if (familia.getRua().trim().length() > 200) {
                throw new IllegalArgumentException("Rua não pode ter mais de 200 caracteres");
            }
        }

        // Número
        if (familia.getNumero() != null && !familia.getNumero().trim().isEmpty()) {
            if (familia.getNumero().trim().length() > 10) {
                throw new IllegalArgumentException("Número não pode ter mais de 10 caracteres");
            }
        }

        // Bairro
        if (familia.getBairro() != null && !familia.getBairro().trim().isEmpty()) {
            if (familia.getBairro().trim().length() > 100) {
                throw new IllegalArgumentException("Bairro não pode ter mais de 100 caracteres");
            }
        }

        // Cidade
        if (familia.getCidade() != null && !familia.getCidade().trim().isEmpty()) {
            if (familia.getCidade().trim().length() < 3) {
                throw new IllegalArgumentException("Cidade deve ter no mínimo 3 caracteres");
            }
            if (familia.getCidade().trim().length() > 100) {
                throw new IllegalArgumentException("Cidade não pode ter mais de 100 caracteres");
            }
        }

        // Data de criação
        if (familia.getData_criacao() != null) {
            Date dataAtual = new Date(System.currentTimeMillis());
            if (familia.getData_criacao().after(dataAtual)) {
                throw new IllegalArgumentException("Data de criação não pode ser no futuro");
            }
        }

        // Verificar se tem membro responsável (apenas na atualização ou quando já existe o ID)
        if (validarResponsavel && familia.getId_familia() > 0) {
            if (!membroAdultoRepository.existeResponsavel(familia.getId_familia())) {
                throw new IllegalArgumentException("A família deve ter pelo menos um membro responsável adulto");
            }
        }
    }
}