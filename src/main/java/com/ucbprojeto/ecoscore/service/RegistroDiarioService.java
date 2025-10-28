package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.model.RegistroDiario;
import com.ucbprojeto.ecoscore.repository.MembroRepository;
import com.ucbprojeto.ecoscore.repository.PraticaSustentavelRepository;
import com.ucbprojeto.ecoscore.repository.RegistroDiarioRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RegistroDiarioService {
    private final RegistroDiarioRepository repository;
    private final MembroRepository membroRepository;
    private final PraticaSustentavelRepository praticaRepository;

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");

    public RegistroDiarioService(RegistroDiarioRepository repository,
                                 MembroRepository membroRepository,
                                 PraticaSustentavelRepository praticaRepository) {
        this.repository = repository;
        this.membroRepository = membroRepository;
        this.praticaRepository = praticaRepository;
    }

    // Listar todos
    public List<RegistroDiario> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public RegistroDiario listarPorId(int id) {
        return repository.findById(id);
    }

    // Listar por membro
    public List<RegistroDiario> listarPorMembro(String cpf_membro) {
        return repository.findByCpf(cpf_membro);
    }

    // CRUD
    public void salvar(RegistroDiario registroDiario) {
        validar(registroDiario);
        repository.save(registroDiario);
    }

    public void atualizar(RegistroDiario registroDiario) {
        validar(registroDiario);
        repository.update(registroDiario);
    }

    public void deletar(int id) {
        repository.delete(id);
    }

    private void validar(RegistroDiario registro) {
        if (registro == null) {
            throw new IllegalArgumentException("Registro diário não pode ser nulo");
        }

        // CPF do membro
        if (registro.getCpf_membro() == null || registro.getCpf_membro().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do membro é obrigatório");
        }

        // Validar formato do CPF
        String cpfLimpo = registro.getCpf_membro().replaceAll("[^0-9]", "");
        if (!CPF_PATTERN.matcher(cpfLimpo).matches()) {
            throw new IllegalArgumentException("CPF inválido. Deve conter exatamente 11 dígitos numéricos");
        }

        // Validar CPFs com todos os dígitos iguais
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido");
        }

        // Verifica se o membro existe
        if (membroRepository.findById(registro.getCpf_membro()) == null) {
            throw new IllegalArgumentException("Membro informado não existe");
        }

        // ID da prática
        if (registro.getId_pratica() <= 0) {
            throw new IllegalArgumentException("ID da prática é obrigatório e deve ser maior que zero");
        }

        // Verifica se a prática existe
        if (praticaRepository.findById(registro.getId_pratica()) == null) {
            throw new IllegalArgumentException("Prática sustentável informada não existe");
        }

        // Data do registro
        if (registro.getData_registro() == null) {
            throw new IllegalArgumentException("Data do registro é obrigatória");
        }

        // Validar se a data não está no futuro
        Date dataAtual = new Date(System.currentTimeMillis());
        if (registro.getData_registro().after(dataAtual)) {
            throw new IllegalArgumentException("Data do registro não pode ser no futuro");
        }

        // Validar se a data não é muito antiga (opcional - 1 ano)
        long umAnoEmMilis = 365L * 24 * 60 * 60 * 1000;
        Date umAnoAtras = new Date(System.currentTimeMillis() - umAnoEmMilis);
        if (registro.getData_registro().before(umAnoAtras)) {
            throw new IllegalArgumentException("Data do registro não pode ser mais de 1 ano no passado");
        }

        // Quantidade realizada
        if (registro.getQuantidade_realizada() <= 0) {
            throw new IllegalArgumentException("Quantidade realizada deve ser maior que zero");
        }

        if (registro.getQuantidade_realizada() > 1000) {
            throw new IllegalArgumentException("Quantidade realizada não pode ser maior que 1000");
        }

        // Pontos ganhos
        if (registro.getPontos_ganhos() < 0) {
            throw new IllegalArgumentException("Pontos ganhos não pode ser negativo");
        }

        if (registro.getPontos_ganhos() > 100000) {
            throw new IllegalArgumentException("Pontos ganhos não pode ser maior que 100000");
        }
    }
}