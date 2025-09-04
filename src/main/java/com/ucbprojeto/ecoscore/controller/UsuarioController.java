package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.Usuario;
import com.ucbprojeto.ecoscore.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios") // rota base
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ------------------ ROTAS ------------------

    // GET /usuarios → listar todos
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    // GET /usuarios/{id} → buscar por id
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable int id) {
        return service.buscarPorId(id);
    }

    // POST /usuarios → criar novo usuário
    @PostMapping
    public String salvar(@RequestBody Usuario usuario) {
        service.salvar(usuario);
        return "Usuário criado com sucesso!";
    }

    // PUT /usuarios/{id} → atualizar usuário existente
    @PutMapping("/{id}")
    public String atualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        service.atualizar(usuario);
        return "Usuário atualizado com sucesso!";
    }

    // DELETE /usuarios/{id} → remover usuário
    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) {
        service.deletar(id);
        return "Usuário deletado com sucesso!";
    }
}