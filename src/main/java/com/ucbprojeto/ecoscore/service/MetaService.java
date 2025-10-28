package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.Meta;
import com.ucbprojeto.ecoscore.repository.FamiliaRepository;
import com.ucbprojeto.ecoscore.repository.MetaRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class MetaService {
    private final MetaRepository repository;
    private final FamiliaRepository familiaRepository;

    public MetaService(MetaRepository repository, FamiliaRepository familiaRepository) {
        this.repository = repository;
        this.familiaRepository = familiaRepository;
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
        validar(meta);
        repository.save(meta);
    }

    public void atualizar(Meta meta) {
        validar(meta);
        repository.update(meta);
    }

    public void deletar(int id) {
        repository.delete(id);
    }

    private void validar(Meta meta) {
        if (meta == null) {
            throw new IllegalArgumentException("Meta não pode ser nula");
        }

        // Título
        if (meta.getTitulo() == null || meta.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título da meta é obrigatório");
        }

        if (meta.getTitulo().trim().length() < 3) {
            throw new IllegalArgumentException("Título da meta deve ter no mínimo 3 caracteres");
        }

        if (meta.getTitulo().trim().length() > 150) {
            throw new IllegalArgumentException("Título da meta não pode ter mais de 150 caracteres");
        }

        // Descrição
        if (meta.getDescricao() == null || meta.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da meta é obrigatória");
        }

        if (meta.getDescricao().trim().length() < 10) {
            throw new IllegalArgumentException("Descrição da meta deve ter no mínimo 10 caracteres");
        }

        if (meta.getDescricao().trim().length() > 500) {
            throw new IllegalArgumentException("Descrição da meta não pode ter mais de 500 caracteres");
        }

        // Pontos objetivo
        if (meta.getPontos_objetivo() <= 0) {
            throw new IllegalArgumentException("Pontos objetivo deve ser maior que zero");
        }

        if (meta.getPontos_objetivo() > 100000) {
            throw new IllegalArgumentException("Pontos objetivo não pode ser maior que 100000");
        }

        // Data início
        if (meta.getData_inicio() == null) {
            throw new IllegalArgumentException("Data de início é obrigatória");
        }

        // Data fim
        if (meta.getData_fim() == null) {
            throw new IllegalArgumentException("Data de fim é obrigatória");
        }

        // Validar ordem das datas
        if (meta.getData_fim().before(meta.getData_inicio())) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início");
        }

        // Validar se a data de início não é muito antiga (opcional)
        long umAnoEmMilis = 365L * 24 * 60 * 60 * 1000;
        Date umAnoAtras = new Date(System.currentTimeMillis() - umAnoEmMilis);
        if (meta.getData_inicio().before(umAnoAtras)) {
            throw new IllegalArgumentException("Data de início não pode ser mais de 1 ano no passado");
        }

        // Validar duração máxima da meta (opcional - 2 anos)
        long doisAnosEmMilis = 2 * 365L * 24 * 60 * 60 * 1000;
        Date dataMaxima = new Date(meta.getData_inicio().getTime() + doisAnosEmMilis);
        if (meta.getData_fim().after(dataMaxima)) {
            throw new IllegalArgumentException("Meta não pode ter duração superior a 2 anos");
        }

        // Status
        if (meta.getStatus() == null) {
            throw new IllegalArgumentException("Status da meta é obrigatório");
        }

        // Validar se é um valor válido do enum
        boolean statusValido = false;
        for (Meta.EStatus s : Meta.EStatus.values()) {
            if (s == meta.getStatus()) {
                statusValido = true;
                break;
            }
        }
        if (!statusValido) {
            throw new IllegalArgumentException("Status inválido. Use: ATIVA, CONCLUIDA ou CANCELADA");
        }

        // ID da família
        if (meta.getId_familia() <= 0) {
            throw new IllegalArgumentException("ID da família é obrigatório e deve ser maior que zero");
        }

        // Verifica se a família existe
        if (familiaRepository.findById(meta.getId_familia()) == null) {
            throw new IllegalArgumentException("Família informada não existe");
        }
    }
}
