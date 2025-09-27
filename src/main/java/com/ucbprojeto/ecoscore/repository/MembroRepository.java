package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Membro;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MembroRepository  {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Membro> rowMapper = (rs, rowNum) -> {
        Membro membro = new Membro();
        membro.setCpf(rs.getString("cpf"));
        membro.setSenha(rs.getString("senha"));
        membro.setId_familia(rs.getInt("id_familia"));
        membro.setNome(rs.getString("nome"));
        membro.setData_nascimento(rs.getDate("data_nascimento"));
        membro.setPapel_familia(rs.getString("papel_familia"));
        membro.setCpf_supervisor(rs.getString("cpf_supervisor"));
        return membro;
    };

    public MembroRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Membro> findAll() {
        String sql = "SELECT * FROM membro";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Membro findById(String cpf) {
        String sql = "SELECT * FROM membro WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, cpf);
    }
    public List<Membro> findByFamily(int id_familia) {
        String sql = "SELECT * FROM membro WHERE id_familia = ?";
        return jdbcTemplate.query(sql, rowMapper, id_familia);
    }
    public void save(Membro membro) {
        String sql = "INSERT INTO membro (cpf, senha, id_familia, nome, data_nascimento, papel_familia, cpf_supervisor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, membro.getCpf(), membro.getSenha(), membro.getId_familia(), membro.getNome(), membro.getData_nascimento(), membro.getPapel_familia(), membro.getCpf_supervisor());
    }
    public void update(Membro membro) {
        String sql = "UPDATE membro SET id_familia = ?, nome = ?, data_nascimento = ?, papel_familia = ?, cpf_supervisor = ? WHERE cpf = ?";
        jdbcTemplate.update(sql, membro.getId_familia(), membro.getNome(), membro.getData_nascimento(), membro.getPapel_familia(), membro.getCpf_supervisor(), membro.getCpf());
    }
    public void delete(String cpf) {
        String sql = "DELETE FROM membro WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }


}
