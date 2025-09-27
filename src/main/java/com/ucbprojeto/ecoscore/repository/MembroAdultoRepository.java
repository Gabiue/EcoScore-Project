package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.MembroAdulto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MembroAdultoRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MembroAdulto> rowMapper = (rs, rowNum) -> {
        MembroAdulto membroAdulto = new MembroAdulto();
        membroAdulto.setCpf(rs.getString("cpf"));
        membroAdulto.setEh_responsavel(rs.getBoolean("eh_responsavel"));
        return membroAdulto;
    };

    public MembroAdultoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<MembroAdulto> findAll() {
        String sql = "SELECT * FROM membro_adulto";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public MembroAdulto findByCpf(String cpf) {
        String sql = "SELECT * FROM membro_adulto WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cpf );
    }
    public boolean existeResponsavel(int id_familia) {
        String sql = "SELECT COUNT(*) FROM membro_adulto ma " +
                "JOIN membro m ON ma.cpf = m.cpf " +
                "WHERE m.id_familia = ? AND ma.eh_responsavel = TRUE";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id_familia);
        return count != null && count > 0;
    }
    public void save(MembroAdulto membroAdulto) {
        String sql = "INSERT INTO membro_adulto (cpf, eh_responsavel) VALUES (?, ?)";
        jdbcTemplate.update(sql, membroAdulto.getCpf(), membroAdulto.isEh_responsavel());
    }
    public void update(MembroAdulto membroAdulto) {
        String sql = "UPDATE membro_adulto SET eh_responsavel = ? WHERE cpf = ?";
        jdbcTemplate.update(sql, membroAdulto.isEh_responsavel(), membroAdulto.getCpf());
    }
    public void deleteBycpf(String cpf) {
        String sql = "DELETE FROM membro_adulto WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }


}
