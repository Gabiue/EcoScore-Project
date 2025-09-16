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

import java.util.List;

@Slf4j
@Service
public class MembroService {
    private final MembroRepository membroRepository;
    private final MembroAdultoRepository membroAdultoRepository;
    private final MembroCriancaRepository membroCriancaRepository;
    private final FamiliaRepository familiaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MembroService(MembroRepository membroRepository, MembroAdultoRepository membroAdultoRepository, MembroCriancaRepository membroCriancaRepository, FamiliaRepository familiaRepository) {
        this.membroRepository = membroRepository;
        this.membroAdultoRepository = membroAdultoRepository;
        this.membroCriancaRepository = membroCriancaRepository;
        this.familiaRepository = familiaRepository;
    }

    //LISTAR TODOS
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
        membroRepository.save(membro);
    }

    public void atualizar(Membro membro) {
        membroRepository.update(membro);
    }

    public void deletar(String cpf) {
        membroRepository.delete(cpf);
    }

    public boolean registrarMembro(RegistroMembroDTO dto) {
        try {
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
}
