package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Telefone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TelefoneRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Telefone> rowMapper =(rs, rowNum) -> {
        Telefone telefone = new Telefone();
        telefone.setId_telefone(rs.getInt("id_telefone"));
        telefone.setNumero(rs.getString("numero"));
        telefone.setCpf_membro(rs.getString("cpf_membro"));
        return telefone;
    };
    public TelefoneRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Telefone> findAll() {
        String sql = "SELECT * FROM telefone";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public Telefone findById(int id) {
        String sql = "SELECT * FROM telefone WHERE id_telefone = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public List<Telefone> findByMembroCpf(String cpf){
        String sql = "SELECT * FROM telefone WHERE cpf_membro = ?";
        return jdbcTemplate.query(sql, rowMapper, cpf);
    }
    public void save(Telefone telefone) {
        String sql = "INSERT INTO telefone (numero, cpf_membro) VALUES (?, ?)";
        jdbcTemplate.update(sql, telefone.getNumero(), telefone.getCpf_membro());
    }

    public void update(Telefone telefone) {
        String sql = "UPDATE telefone SET numero = ?, cpf_membro = ? WHERE id_telefone = ?";
        jdbcTemplate.update(sql, telefone.getNumero(), telefone.getCpf_membro(), telefone.getId_telefone());
    }
    public void delete(int id) {
        String sql = "DELETE FROM telefone WHERE id_telefone = ?";
        jdbcTemplate.update(sql, id);
    }



}
