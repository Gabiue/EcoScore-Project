package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.RegistroDiario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegistroDiarioRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RegistroDiario> rowMapper = (rs, rowNum) -> {
        RegistroDiario registroDiario = new RegistroDiario();
        registroDiario.setId_registro(rs.getInt("id_registro"));
        registroDiario.setCpf_membro(rs.getString("cpf_membro"));
        registroDiario.setId_pratica(rs.getInt("id_pratica"));
        registroDiario.setData_registro(rs.getDate("data_registro"));
        registroDiario.setQuantidade_realizada(rs.getInt("quantidade_realizada"));
        registroDiario.setPontos_ganhos(rs.getInt("pontos_ganhos"));
        return registroDiario;
    };

    public RegistroDiarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RegistroDiario> findAll() {
        String sql = "SELECT * FROM registro_diario";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public RegistroDiario findById(int id_registro) {
        String sql = "SELECT * FROM registro_diario WHERE id_registro = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id_registro);
    }

    public List<RegistroDiario> findByCpf(String cpf_membro) {
        String sql = "SELECT * FROM registro_diario WHERE cpf_membro = ?";
        return jdbcTemplate.query(sql, rowMapper, cpf_membro);
    }

    public void save(RegistroDiario registroDiario) {
        String sql = "INSERT INTO registro_diario (cpf_membro, data_registro, id_pratica, quantidade_realizada, pontos_ganhos) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, registroDiario.getCpf_membro(), registroDiario.getData_registro(), registroDiario.getId_pratica(), registroDiario.getQuantidade_realizada(), registroDiario.getPontos_ganhos());
    }

    public void update(RegistroDiario registroDiario) {
        String sql = "UPDATE registro_diario SET cpf_membro = ?, data_registro = ?, id_pratica = ?, quantidade_realizada = ?, pontos_ganhos = ? WHERE id_registro = ?";
        jdbcTemplate.update(sql, registroDiario.getCpf_membro(), registroDiario.getData_registro(), registroDiario.getId_pratica(), registroDiario.getQuantidade_realizada(), registroDiario.getPontos_ganhos(), registroDiario.getId_registro());
    }

    public void delete(int id_registro) {
        String sql = "DELETE FROM registro_diario WHERE id_registro = ?";
        jdbcTemplate.update(sql, id_registro);
    }
}
