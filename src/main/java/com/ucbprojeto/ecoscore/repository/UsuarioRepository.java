package com.ucbprojeto.ecoscore.repository;

import com.ucbprojeto.ecoscore.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Listar todos os usuários
    public List<Usuario> findAll() {
        String sql = "SELECT id, nome, email FROM usuarios";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            return u;
        });
    }

    // Buscar usuário por ID
    public Usuario findById(int id) {
        String sql = "SELECT id, nome, email FROM usuarios WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            return u;
        });
    }

    // Salvar novo usuário
    public int save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";
        return jdbcTemplate.update(sql, usuario.getNome(), usuario.getEmail());
    }

    // Atualizar usuário existente
    public int update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getId());
    }

    // Deletar usuário pelo ID
    public int delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}