package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Conquista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConquistaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Conquista> rowMapper = (rs, rowNum) ->{
        Conquista conquista = new Conquista();
        conquista.setId_conquista(rs.getInt("id_conquista"));
        conquista.setId_meta(rs.getInt("id_meta"));
        conquista.setData_conquista(rs.getDate("data_conquista"));
        conquista.setPontos_bonus(rs.getInt("pontos_bonus"));
        return conquista;
    };

    public ConquistaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //Primary key composta com id_conquista e id_meta;

    public List<Conquista>  findAll() {
        String sql = "SELECT * FROM conquista";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public Conquista findById(int id_conquista, int id_meta) {
        String sql = "SELECT * FROM conquista WHERE id_conquista = ? AND id_meta = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id_conquista, id_meta);
    }
    public void save(Conquista conquista){
        String sql = "INSERT INTO conquista (id_conquista, id_meta, data_conquista, pontos_bonus) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, conquista.getId_conquista(), conquista.getId_meta(), conquista.getData_conquista(), conquista.getPontos_bonus());
    }
    public void update(Conquista conquista){
        String sql = "UPDATE conquista SET data_conquista = ?, pontos_bonus = ? WHERE id_conquista = ? AND id_meta = ?";
        jdbcTemplate.update(sql, conquista.getData_conquista(), conquista.getPontos_bonus(), conquista.getId_conquista(), conquista.getId_meta());
    }
    public void delete(int id_conquista, int id_meta){
        String sql = "DELETE FROM conquista WHERE id_conquista = ? AND id_meta = ?";
        jdbcTemplate.update(sql, id_conquista, id_meta);
    }
}
