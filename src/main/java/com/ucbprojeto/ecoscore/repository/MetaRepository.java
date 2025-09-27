package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Meta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Meta> rowMapper = (rs, rowNum) -> {
        Meta meta = new Meta();
        meta.setId_meta(rs.getInt("id_meta"));
        meta.setId_familia(rs.getInt("id_familia"));
        meta.setTitulo(rs.getString("titulo"));
        meta.setDescricao(rs.getString("descricao"));
        meta.setPontos_objetivo(rs.getInt("pontos_objetivo"));
        meta.setData_inicio(rs.getDate("data_inicio"));
        meta.setData_fim(rs.getDate("data_fim"));
        meta.setStatus(Meta.EStatus.valueOf(rs.getString("status")));
        return meta;
    };

    public MetaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Meta> findAll() {
        String sql = "SELECT * FROM meta";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Meta findById(int id) {
        String sql = "SELECT * FROM meta WHERE id_meta = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public List<Meta> findByFamilyId(int familyId) {
        String sql = "SELECT * FROM meta WHERE id_familia = ?";
        return jdbcTemplate.query(sql, rowMapper, familyId);
    }
    public List<Meta> findByStatus(Meta.EStatus status) {
        String sql = "SELECT * FROM meta WHERE status = ?";
        return jdbcTemplate.query(sql, rowMapper, status.name());
    }
    public void save(Meta meta){
        String sql = "INSERT INTO meta (id_familia, titulo, descricao, pontos_objetivo, data_inicio, data_fim, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, meta.getId_familia(), meta.getTitulo(), meta.getDescricao(), meta.getPontos_objetivo(), meta.getData_inicio(), meta.getData_fim(), meta.getStatus().name());
    }
    public void update(Meta meta){
        String sql = "UPDATE meta SET id_familia = ?, titulo = ?, descricao = ?, pontos_objetivo = ?, data_inicio = ?, data_fim = ?, status = ? WHERE id_meta = ?";
        jdbcTemplate.update(sql, meta.getId_familia(), meta.getTitulo(), meta.getDescricao(), meta.getPontos_objetivo(), meta.getData_inicio(), meta.getData_fim(), meta.getStatus().name(), meta.getId_meta());
    }
    public void delete (int id){
        String sql = "DELETE FROM meta WHERE id_meta = ?";
        jdbcTemplate.update(sql, id);
    }
}
