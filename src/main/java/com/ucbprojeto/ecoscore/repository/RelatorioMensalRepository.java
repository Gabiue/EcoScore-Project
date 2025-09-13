package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.RelatorioMensal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatorioMensalRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RelatorioMensal> rowMapper = (rs, rowNum) ->{
        RelatorioMensal relatorioMensal = new RelatorioMensal();
        relatorioMensal.setId_relatorio(rs.getInt("id_relatorio"));
        relatorioMensal.setId_familia(rs.getInt("id_familia"));
        relatorioMensal.setMes_ano(rs.getDate("mes_ano"));
        relatorioMensal.setTotal_pontos_familia(rs.getInt("total_pontos_familia"));
        relatorioMensal.setTotal_praticas_realizadas(rs.getInt("total_praticas_realizadas"));
        return relatorioMensal;
    };

    public RelatorioMensalRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RelatorioMensal> findAll(){
        String sql = "SELECT * FROM relatorio_mensal";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public RelatorioMensal findById(int id_relatorio){
        String sql = "SELECT * FROM relatorio_mensal WHERE id_relatorio = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id_relatorio);
    }
    public List<RelatorioMensal> findByFamilia(int id_familia){
        String sql = "SELECT * FROM relatorio_mensal WHERE id_familia = ?";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void save(RelatorioMensal relatorioMensal){
        String sql = "INSERT INTO relatorio_mensal (id_familia, mes_ano, total_pontos_familia, total_praticas_realizadas) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, relatorioMensal.getId_familia(), relatorioMensal.getMes_ano(), relatorioMensal.getTotal_pontos_familia(), relatorioMensal.getTotal_praticas_realizadas());
    }

    public void update(RelatorioMensal relatorioMensal){
        String sql = "UPDATE relatorio_mensal SET id_familia = ?, mes_ano = ?, total_pontos_familia = ?, total_praticas_realizadas = ? WHERE id_relatorio = ?";
        jdbcTemplate.update(sql, relatorioMensal.getId_familia(), relatorioMensal.getMes_ano(), relatorioMensal.getTotal_pontos_familia(), relatorioMensal.getTotal_praticas_realizadas(), relatorioMensal.getId_relatorio());
    }

    public void deleteById(int id_relatorio){
        String sql = "DELETE FROM relatorio_mensal WHERE id_relatorio = ?";
        jdbcTemplate.update(sql, id_relatorio);
    }



}
