package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Familia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class FamiliaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Familia> rowMapper = (rs, rowNum) -> {
        Familia familia = new Familia();
        familia.setId_familia(rs.getInt("id_familia"));
        familia.setNome(rs.getString("nome"));
        familia.setRua(rs.getString("rua"));
        familia.setNumero(rs.getString("numero"));
        familia.setBairro(rs.getString("bairro"));
        familia.setCidade(rs.getString("cidade"));
        familia.setCep(rs.getString("cep"));
        familia.setData_criacao(rs.getDate("data_criacao"));
        return familia;
    };

    public FamiliaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Familia> findAll() {
        String sql = "SELECT * FROM familia";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public Familia findById(int id) {
        String sql = "SELECT * FROM familia WHERE id_familia = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public void save(Familia familia) {
        String sql = "INSERT INTO familia (nome, rua, numero, bairro, cidade, cep, data_criacao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, familia.getNome(), familia.getRua(), familia.getNumero(), familia.getBairro(), familia.getCidade(), familia.getCep(), familia.getData_criacao());
    }
    public void update(Familia familia){
        String sql = "UPDATE familia SET nome = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, cep = ?, data_criacao = ? WHERE id_familia = ?";
        jdbcTemplate.update(sql, familia.getNome(), familia.getRua(), familia.getNumero(), familia.getBairro(), familia.getCidade(), familia.getCep(), familia.getData_criacao(), familia.getId_familia());
    }
    public void delete(int id) {
        String sql = "DELETE FROM familia WHERE id_familia = ?";
        jdbcTemplate.update(sql, id);
    }

}
