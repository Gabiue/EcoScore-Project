package com.ucbprojeto.ecoscore.repository;


import com.ucbprojeto.ecoscore.model.PraticaSustentavel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PraticaSustentavelRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PraticaSustentavel> rowMapper = (rs, rowNum) -> {
        PraticaSustentavel pratica = new PraticaSustentavel();
        pratica.setId_pratica(rs.getInt("id_pratica"));
        pratica.setNome(rs.getString("nome"));
        pratica.setDescricao(rs.getString("descricao"));
        pratica.setId_categoria(rs.getInt("id_categoria"));
        pratica.setDificuldade(PraticaSustentavel.EDificuldade.valueOf(rs.getString("dificuldade")));
        pratica.setPontos_base(rs.getInt("pontos_base"));
        pratica.setFrequencia_esperada(rs.getString("frequencia_esperada"));
        return pratica;
    };

    public PraticaSustentavelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PraticaSustentavel> findAll() {
        String sql = "SELECT * FROM pratica_sustentavel";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public PraticaSustentavel findById(int id) {
        String sql = "SELECT * FROM pratica_sustentavel WHERE id_pratica = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public List<PraticaSustentavel> findByCategory(int id_categoria) {
        String sql = "SELECT * FROM pratica_sustentavel WHERE id_categoria = ?";
        return jdbcTemplate.query(sql, rowMapper, id_categoria);
    }
    public List<PraticaSustentavel> findByDificuldade(PraticaSustentavel.EDificuldade dificuldade) {
        String sql = "SELECT * FROM pratica_sustentavel WHERE dificuldade = ?";
        return jdbcTemplate.query(sql, rowMapper, dificuldade.name());
    }
   public void save(PraticaSustentavel pratica) {
        String sql = "INSERT INTO pratica_sustentavel (id_categoria, nome, descricao, pontos_base, dificuldade, frequencia_esperada) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, pratica.getId_categoria(), pratica.getNome(), pratica.getDescricao(), pratica.getPontos_base(), pratica.getDificuldade().name(), pratica.getFrequencia_esperada());
    }

    public void update(PraticaSustentavel pratica) {
        String sql = "UPDATE pratica_sustentavel SET id_categoria = ?, nome = ?, descricao = ?, pontos_base = ?, dificuldade = ?, frequencia_esperada = ? WHERE id_pratica = ?";
        jdbcTemplate.update(sql, pratica.getId_categoria(), pratica.getNome(), pratica.getDescricao(), pratica.getPontos_base(), pratica.getDificuldade().name(), pratica.getFrequencia_esperada(), pratica.getId_pratica());
    }

    public void delete(int id) {
        String sql = "DELETE FROM pratica_sustentavel WHERE id_pratica = ?";
        jdbcTemplate.update(sql, id);
    }
}
