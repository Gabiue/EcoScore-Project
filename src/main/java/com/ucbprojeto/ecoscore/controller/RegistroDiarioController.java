package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.RegistroDiario;
import com.ucbprojeto.ecoscore.service.RegistroDiarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registro-diario")
public class RegistroDiarioController {

    @Autowired
    public RegistroDiarioService registroDiarioService;

    private static final Logger logger = LoggerFactory.getLogger(RegistroDiarioController.class);

    @GetMapping
    public ResponseEntity<List<RegistroDiario>> listarTodos() {
        try {
            List<RegistroDiario> registros = registroDiarioService.listarTodos();
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            logger.error("Erro ao listar registros diários", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDiario> buscarPorId(@PathVariable int id) {
        try {
            RegistroDiario registro = registroDiarioService.listarPorId(id);
            return ResponseEntity.ok(registro);
        } catch (Exception e) {
            logger.error("Erro ao buscar registro diário com id = {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/membro/{cpf_membro}")
    public ResponseEntity<List<RegistroDiario>> listarPorMembro(@PathVariable String cpf_membro) {
        try {
            List<RegistroDiario> registros = registroDiarioService.listarPorMembro(cpf_membro);
            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            logger.error("Erro ao listar registros diários do membro com CPF = {}", cpf_membro, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarRegistro(@RequestBody RegistroDiario registroDiario) {
        try {
            registroDiarioService.salvar(registroDiario);
            logger.info("Registro diário criado com sucesso: CPF={}, id_pratica={}", registroDiario.getCpf_membro(), registroDiario.getId_pratica());
            return ResponseEntity.ok("Registro diário criado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar registro diário: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar registro diário", e);
            return ResponseEntity.internalServerError().body("Erro ao criar registro diário");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody RegistroDiario registroDiario) {
        try {
            registroDiario.setId_registro(id);
            registroDiarioService.atualizar(registroDiario);
            logger.info("Registro diário atualizado com sucesso: id={}", id);
            return ResponseEntity.ok("Registro diário atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar registro diário com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar registro diário com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar registro diário");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable int id) {
        try {
            registroDiarioService.deletar(id);
            logger.info("Registro diário deletado com sucesso: id={}", id);
            return ResponseEntity.ok("Registro diário deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar registro diário com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar registro diário");
        }
    }
}