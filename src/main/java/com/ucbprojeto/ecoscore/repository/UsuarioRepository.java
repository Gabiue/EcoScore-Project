package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UsuarioRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Usuario> rowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        return usuario;
    };

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuario";
        return jdbcTemplate.query(sql, rowMapper);
    }
    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, usuario.getNome(), usuario.getEmail());
    }
    public void update (Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getId());
    }
    public void delete(int id){
        String sql = "DELETE FROM usuario WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


}