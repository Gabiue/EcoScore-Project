package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.Telefone;
import com.ucbprojeto.ecoscore.service.TelefoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telefone")
public class TelefoneController {

    @Autowired
    public TelefoneService telefoneService;

    private static final Logger logger = LoggerFactory.getLogger(TelefoneController.class);

    @GetMapping
    public ResponseEntity<List<Telefone>> listarTodos() {
        try {
            List<Telefone> telefones = telefoneService.listarTodos();
            return ResponseEntity.ok(telefones);
        } catch (Exception e) {
            logger.error("Erro ao listar telefones", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/membro/{cpf}")
    public ResponseEntity<List<Telefone>> buscarPorCpf(@PathVariable String cpf) {
        try {
            List<Telefone> telefones = telefoneService.buscarPorCpf(cpf);
            return ResponseEntity.ok(telefones);
        } catch (Exception e) {
            logger.error("Erro ao buscar telefones do membro com CPF = {}", cpf, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarTelefone(@RequestBody Telefone telefone) {
        try {
            telefoneService.salvar(telefone);
            logger.info("Telefone criado com sucesso: CPF={}, número={}", telefone.getCpf_membro(), telefone.getNumero());
            return ResponseEntity.ok("Telefone criado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar telefone: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar telefone", e);
            return ResponseEntity.internalServerError().body("Erro ao criar telefone");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Telefone telefone) {
        try {
            telefone.setId_telefone(id);
            telefoneService.atualizar(telefone);
            logger.info("Telefone atualizado com sucesso: id={}", id);
            return ResponseEntity.ok("Telefone atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar telefone com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar telefone com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar telefone");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable int id) {
        try {
            telefoneService.deletar(id);
            logger.info("Telefone deletado com sucesso: id={}", id);
            return ResponseEntity.ok("Telefone deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar telefone com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar telefone");
        }
    }
}
