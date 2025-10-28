package com.ucbprojeto.ecoscore.service;

import com.ucbprojeto.ecoscore.dto.RegistroMembroDTO;
import com.ucbprojeto.ecoscore.model.Membro;
import com.ucbprojeto.ecoscore.model.MembroAdulto;
import com.ucbprojeto.ecoscore.model.MembroCrianca;
import com.ucbprojeto.ecoscore.repository.FamiliaRepository;
import com.ucbprojeto.ecoscore.repository.MembroAdultoRepository;
import com.ucbprojeto.ecoscore.repository.MembroCriancaRepository;
import com.ucbprojeto.ecoscore.repository.MembroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MembroService {
    private final MembroRepository membroRepository;
    private final MembroAdultoRepository membroAdultoRepository;
    private final MembroCriancaRepository membroCriancaRepository;
    private final FamiliaRepository familiaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public MembroService(MembroRepository membroRepository, MembroAdultoRepository membroAdultoRepository,
                         MembroCriancaRepository membroCriancaRepository, FamiliaRepository familiaRepository) {
        this.membroRepository = membroRepository;
        this.membroAdultoRepository = membroAdultoRepository;
        this.membroCriancaRepository = membroCriancaRepository;
        this.familiaRepository = familiaRepository;
    }

    // LISTAR TODOS
    public List<Membro> listar() {
        return membroRepository.findAll();
    }

    public List<Membro> listarPorFamilia(int id_familia) {
        return membroRepository.findByFamily(id_familia);
    }

    public Membro buscarPorId(String cpf) {
        return membroRepository.findById(cpf);
    }

    public void salvar(Membro membro) {
        validarMembro(membro);
        membroRepository.save(membro);
    }

    public void atualizar(Membro membro) {
        validarMembro(membro);
        membroRepository.update(membro);
    }

    public void deletar(String cpf) {
        membroRepository.delete(cpf);
    }

    public boolean registrarMembro(RegistroMembroDTO dto) {
        try {
            // Validar DTO
            validarRegistroMembroDTO(dto);

            // Validação: membro criança precisa de responsável
            if ("crianca".equalsIgnoreCase(dto.getTipo())) {
                if (dto.getCpf_responsavel() == null || dto.getCpf_responsavel().isEmpty()) {
                    log.error("Criança sem responsável: CPF={}", dto.getCpf());
                    return false;
                }
                // Verifica se o responsável existe e é adulto
                MembroAdulto responsavel = null;
                try {
                    responsavel = membroAdultoRepository.findByCpf(dto.getCpf_responsavel());
                } catch (Exception e) {
                    log.error("Responsável não encontrado ou não é adulto: CPF={}", dto.getCpf_responsavel());
                    return false;
                }
                if (responsavel == null) {
                    log.error("Responsável não encontrado: CPF={}", dto.getCpf_responsavel());
                    return false;
                }
            }

            // Cria o membro base
            Membro membro = new Membro();
            membro.setCpf(dto.getCpf());
            membro.setSenha(passwordEncoder.encode(dto.getSenha()));

            // Se id_familia for nulo ou 0, salva como null
            if (dto.getId_familia() == null || dto.getId_familia() == 0) {
                membro.setId_familia(null);
            } else {
                membro.setId_familia(dto.getId_familia());
            }

            membro.setNome(dto.getNome());
            membro.setData_nascimento(dto.getData_nascimento());
            membro.setPapel_familia(dto.getPapel_familia());
            membro.setCpf_supervisor(dto.getCpf_supervisor());

            membroRepository.save(membro);

            if ("adulto".equalsIgnoreCase(dto.getTipo())) {
                MembroAdulto adulto = new MembroAdulto();
                adulto.setCpf(dto.getCpf());
                adulto.setEh_responsavel(Boolean.TRUE.equals(dto.getEh_responsavel()));
                membroAdultoRepository.save(adulto);
            } else if ("crianca".equalsIgnoreCase(dto.getTipo())) {
                MembroCrianca crianca = new MembroCrianca();
                crianca.setCpf(dto.getCpf());
                crianca.setCpf_responsavel(dto.getCpf_responsavel());
                crianca.setBonus_escolar(dto.getBonus_escolar() != null ? dto.getBonus_escolar() : 0);
                membroCriancaRepository.save(crianca);
            } else {
                return false; // Tipo inválido
            }

            return true;
        } catch (Exception e) {
            log.error("e: ", e);
            return false;
        }
    }

    public Membro autenticarMembro(String cpf, String senha) {
        Membro membro = membroRepository.findById(cpf);

        if (membro != null && membro.getSenha() != null && passwordEncoder.matches(senha, membro.getSenha())) {
            return membro;
        }
        return null;
    }

    public boolean vincularMembroAFamilia(String cpf, int id_familia) {
        try {
            // Validar CPF
            validarCpf(cpf);

            // Validar ID da família
            if (id_familia <= 0) {
                throw new IllegalArgumentException("ID da família deve ser maior que zero");
            }

            // Verifica se o membro existe
            Membro membro = null;
            try {
                membro = membroRepository.findById(cpf);
            } catch (Exception e) {
                log.error("Membro não encontrado: CPF={}", cpf);
                return false;
            }
            if (membro == null) {
                log.error("Membro não encontrado: CPF={}", cpf);
                return false;
            }

            // Verifica se a família existe
            try {
                familiaRepository.findById(id_familia);
            } catch (Exception e) {
                log.error("Família não encontrada: id_familia={}", id_familia);
                return false;
            }

            // Atualiza o membro
            membro.setId_familia(id_familia);
            membroRepository.update(membro);
            return true;
        } catch (Exception e) {
            log.error("Erro ao vincular membro à família: ", e);
            return false;
        }
    }

    // ==================== MÉTODOS DE VALIDAÇÃO ====================

    private void validarMembro(Membro membro) {
        if (membro == null) {
            throw new IllegalArgumentException("Membro não pode ser nulo");
        }

        // CPF
        validarCpf(membro.getCpf());

        // Nome
        if (membro.getNome() == null || membro.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do membro é obrigatório");
        }

        if (membro.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome do membro deve ter no mínimo 3 caracteres");
        }

        if (membro.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome do membro não pode ter mais de 100 caracteres");
        }

        // Senha
        if (membro.getSenha() == null || membro.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        // Data de nascimento
        if (membro.getData_nascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        Date dataAtual = new Date(System.currentTimeMillis());
        if (membro.getData_nascimento().after(dataAtual)) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }

        // Validar idade mínima (por exemplo, pelo menos 1 ano atrás)
        long umAnoEmMilis = 365L * 24 * 60 * 60 * 1000;
        Date umAnoAtras = new Date(System.currentTimeMillis() - umAnoEmMilis);
        if (membro.getData_nascimento().after(umAnoAtras)) {
            throw new IllegalArgumentException("Membro deve ter pelo menos 1 ano de idade");
        }

        // Papel na família
        if (membro.getPapel_familia() != null && !membro.getPapel_familia().trim().isEmpty()) {
            if (membro.getPapel_familia().trim().length() > 50) {
                throw new IllegalArgumentException("Papel na família não pode ter mais de 50 caracteres");
            }
        }

        // CPF supervisor (se fornecido)
        if (membro.getCpf_supervisor() != null && !membro.getCpf_supervisor().trim().isEmpty()) {
            validarCpf(membro.getCpf_supervisor());
        }

        // ID da família (se fornecido)
        if (membro.getId_familia() != null && membro.getId_familia() <= 0) {
            throw new IllegalArgumentException("ID da família inválido");
        }
    }

    private void validarRegistroMembroDTO(RegistroMembroDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados de registro não podem ser nulos");
        }

        // CPF
        validarCpf(dto.getCpf());

        // Senha
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        if (dto.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }

        if (dto.getSenha().length() > 100) {
            throw new IllegalArgumentException("Senha não pode ter mais de 100 caracteres");
        }

        // Nome
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (dto.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        if (dto.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }

        // Data de nascimento
        if (dto.getData_nascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        Date dataAtual = new Date(System.currentTimeMillis());
        if (dto.getData_nascimento().after(dataAtual)) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }

        // Tipo
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de membro é obrigatório");
        }

        String tipo = dto.getTipo().toLowerCase();
        if (!tipo.equals("adulto") && !tipo.equals("crianca")) {
            throw new IllegalArgumentException("Tipo de membro inválido. Use: adulto ou crianca");
        }

        // Validações específicas para criança
        if (tipo.equals("crianca")) {
            if (dto.getCpf_responsavel() == null || dto.getCpf_responsavel().trim().isEmpty()) {
                throw new IllegalArgumentException("CPF do responsável é obrigatório para crianças");
            }
            validarCpf(dto.getCpf_responsavel());

            if (dto.getBonus_escolar() != null && dto.getBonus_escolar() < 0) {
                throw new IllegalArgumentException("Bônus escolar não pode ser negativo");
            }
        }

        // Papel na família
        if (dto.getPapel_familia() != null && !dto.getPapel_familia().trim().isEmpty()) {
            if (dto.getPapel_familia().trim().length() > 50) {
                throw new IllegalArgumentException("Papel na família não pode ter mais de 50 caracteres");
            }
        }

        // CPF supervisor
        if (dto.getCpf_supervisor() != null && !dto.getCpf_supervisor().trim().isEmpty()) {
            validarCpf(dto.getCpf_supervisor());
        }
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }

        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (!CPF_PATTERN.matcher(cpfLimpo).matches()) {
            throw new IllegalArgumentException("CPF inválido. Deve conter exatamente 11 dígitos numéricos");
        }

        // Validar CPFs com todos os dígitos iguais (ex: 111.111.111-11)
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}