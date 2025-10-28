package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.RelatorioMensal;
import com.ucbprojeto.ecoscore.repository.FamiliaRepository;
import com.ucbprojeto.ecoscore.repository.RelatorioMensalRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioMensalService {
    private final RelatorioMensalRepository repository;
    private final FamiliaRepository familiaRepository;

    public RelatorioMensalService(RelatorioMensalRepository repository,
                                  FamiliaRepository familiaRepository) {
        this.repository = repository;
        this.familiaRepository = familiaRepository;
    }

    public List<RelatorioMensal> listarTodos() {
        return repository.findAll();
    }

    public RelatorioMensal buscarPorId(int id_relatorio) {
        return repository.findById(id_relatorio);
    }

    public List<RelatorioMensal> buscarPorFamilia(int id_familia) {
        return repository.findByFamilia(id_familia);
    }

    public void salvar(RelatorioMensal relatorioMensal) {
        validar(relatorioMensal);
        repository.save(relatorioMensal);
    }

    public void atualizar(RelatorioMensal relatorioMensal) {
        validar(relatorioMensal);
        repository.update(relatorioMensal);
    }

    public void deletar(int id_relatorio) {
        repository.deleteById(id_relatorio);
    }

    private void validar(RelatorioMensal relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatório mensal não pode ser nulo");
        }

        // ID da família
        if (relatorio.getId_familia() <= 0) {
            throw new IllegalArgumentException("ID da família é obrigatório e deve ser maior que zero");
        }

        // Verifica se a família existe
        if (familiaRepository.findById(relatorio.getId_familia()) == null) {
            throw new IllegalArgumentException("Família informada não existe");
        }

        // Mês/Ano
        if (relatorio.getMes_ano() == null) {
            throw new IllegalArgumentException("Mês/Ano é obrigatório");
        }

        // Validar se a data não está no futuro
        Date dataAtual = new Date(System.currentTimeMillis());

        // Obter o primeiro dia do mês atual
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDiaMesAtual = hoje.withDayOfMonth(1);
        Date primeiroDiaMesAtualDate = Date.valueOf(primeiroDiaMesAtual);

        // Obter o primeiro dia do mês seguinte
        LocalDate primeiroDiaMesSeguinte = primeiroDiaMesAtual.plusMonths(1);
        Date primeiroDiaMesSeguinteDate = Date.valueOf(primeiroDiaMesSeguinte);

        if (relatorio.getMes_ano().after(primeiroDiaMesSeguinteDate) ||
                relatorio.getMes_ano().equals(primeiroDiaMesSeguinteDate)) {
            throw new IllegalArgumentException("Não é possível criar relatório para mês/ano futuro");
        }

        // Validar se a data não é muito antiga (opcional - 5 anos)
        LocalDate cincoAnosAtras = hoje.minusYears(5).withDayOfMonth(1);
        Date cincoAnosAtrasDate = Date.valueOf(cincoAnosAtras);
        if (relatorio.getMes_ano().before(cincoAnosAtrasDate)) {
            throw new IllegalArgumentException("Não é possível criar relatório para mais de 5 anos no passado");
        }

        // Total de pontos da família
        if (relatorio.getTotal_pontos_familia() < 0) {
            throw new IllegalArgumentException("Total de pontos da família não pode ser negativo");
        }

        if (relatorio.getTotal_pontos_familia() > 1000000) {
            throw new IllegalArgumentException("Total de pontos da família não pode ser maior que 1000000");
        }

        // Total de práticas realizadas
        if (relatorio.getTotal_praticas_realizadas() < 0) {
            throw new IllegalArgumentException("Total de práticas realizadas não pode ser negativo");
        }

        if (relatorio.getTotal_praticas_realizadas() > 100000) {
            throw new IllegalArgumentException("Total de práticas realizadas não pode ser maior que 100000");
        }

        // Validação de consistência: se tem práticas, deve ter pontos (e vice-versa)
        if (relatorio.getTotal_praticas_realizadas() > 0 && relatorio.getTotal_pontos_familia() == 0) {
            throw new IllegalArgumentException("Se há práticas realizadas, deve haver pontos ganhos");
        }
    }
}