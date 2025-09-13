package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.MembroCrianca;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MembroCriancaRepository {
    private final JdbcTemplate jdbcTemplate;

    public MembroCriancaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MembroCrianca> rowMapper = (rs, rowNum) -> {
        MembroCrianca membroCrianca = new MembroCrianca();
        membroCrianca.setCpf(rs.getString("cpf"));
        membroCrianca.setCpf_responsavel(rs.getString("cpf_responsavel"));
        membroCrianca.setBonus_escolar(rs.getInt("bonus_escolar"));
        return membroCrianca;
    };

    public List<MembroCrianca> findAll() {
        String sql = "SELECT * FROM membro_crianca";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public MembroCrianca findByCpf(String cpf) {
        String sql = "SELECT * FROM membro_crianca WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cpf);
    }

    public void save(MembroCrianca membroCrianca) {
        String sql = "INSERT INTO membro_crianca (cpf, cpf_responsavel, bonus_escolar) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, membroCrianca.getCpf(), membroCrianca.getCpf_responsavel(), membroCrianca.getBonus_escolar());
    }

    public void update(MembroCrianca membroCrianca) {
        String sql = "UPDATE membro_crianca SET cpf_responsavel = ?, bonus_escolar = ? WHERE cpf = ?";
        jdbcTemplate.update(sql, membroCrianca.getCpf_responsavel(), membroCrianca.getBonus_escolar(), membroCrianca.getCpf());
    }

    public void delete(String cpf) {
        String sql = "DELETE FROM membro_crianca WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }


}
