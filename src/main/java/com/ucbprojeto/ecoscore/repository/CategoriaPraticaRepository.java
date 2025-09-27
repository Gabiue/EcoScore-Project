package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.CategoriaPratica;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaPraticaRepository {
    private final JdbcTemplate jdbcTemplate;


    private final RowMapper<CategoriaPratica> rowMapper = (rs, rowNum) -> {
        CategoriaPratica categoriaPratica = new CategoriaPratica();
        categoriaPratica.setId_categoria(rs.getInt("id_categoria"));
        categoriaPratica.setNome(rs.getString("nome"));
        categoriaPratica.setFator_multiplicador(rs.getDouble("fator_multiplicador"));
        return categoriaPratica;
    };

    public CategoriaPraticaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CategoriaPratica> findAll() {
        String sql = "SELECT * FROM categoria_pratica";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public CategoriaPratica findById(int id) {
        String sql = "SELECT * FROM categoria_pratica WHERE id_categoria = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void save(CategoriaPratica categoriaPratica) {
        String sql = "INSERT INTO categoria_pratica (nome, fator_multiplicador) VALUES (?, ?)";
        jdbcTemplate.update(sql, categoriaPratica.getNome(), categoriaPratica.getFator_multiplicador());
    }
    public void update(CategoriaPratica categoriaPratica) {
        String sql = "UPDATE categoria_pratica SET nome = ?, fator_multiplicador = ? WHERE id_categoria = ?";
        jdbcTemplate.update(sql, categoriaPratica.getNome(), categoriaPratica.getFator_multiplicador(), categoriaPratica.getId_categoria());
    }
    public void delete(int id) {
        String sql = "DELETE FROM categoria_pratica WHERE id_categoria = ?";
        jdbcTemplate.update(sql, id);
    }

}
