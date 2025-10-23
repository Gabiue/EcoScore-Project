package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.Conquista;
import com.ucbprojeto.ecoscore.service.ConquistaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conquista")
public class ConquistaController {

    @Autowired
    public ConquistaService conquistaService;

    private static final Logger logger = LoggerFactory.getLogger(ConquistaController.class);

    @GetMapping
    public ResponseEntity<List<Conquista>> listarTodas() {
        try {
            List<Conquista> conquistas = conquistaService.listarTodos();
            return ResponseEntity.ok(conquistas);
        } catch (Exception e) {
            logger.error("Erro ao listar conquistas", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id_conquista}/{id_meta}")
    public ResponseEntity<Conquista> buscarPorId(@PathVariable int id_conquista, @PathVariable int id_meta) {
        try {
            Conquista conquista = conquistaService.buscarPorId(id_conquista, id_meta);
            return ResponseEntity.ok(conquista);
        } catch (Exception e) {
            logger.error("Erro ao buscar conquista com id_conquista = {} e id_meta = {}", id_conquista, id_meta, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarConquista(@RequestBody Conquista conquista) {
        try {
            conquistaService.salvar(conquista);
            logger.info("Conquista criada com sucesso: id_conquista={}, id_meta={}", conquista.getId_conquista(), conquista.getId_meta());
            return ResponseEntity.ok("Conquista criada com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar conquista: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar conquista", e);
            return ResponseEntity.internalServerError().body("Erro ao criar conquista");
        }
    }

    @PutMapping("/{id_conquista}/{id_meta}")
    public ResponseEntity<?> atualizar(@PathVariable int id_conquista, @PathVariable int id_meta, @RequestBody Conquista conquista) {
        try {
            conquista.setId_conquista(id_conquista);
            conquista.setId_meta(id_meta);
            conquistaService.atualizar(conquista);
            logger.info("Conquista atualizada com sucesso: id_conquista={}, id_meta={}", id_conquista, id_meta);
            return ResponseEntity.ok("Conquista atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar conquista com id_conquista = {} e id_meta = {}: {}", id_conquista, id_meta, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar conquista com id_conquista = {} e id_meta = {}", id_conquista, id_meta, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar conquista");
        }
    }

    @DeleteMapping("/{id_conquista}/{id_meta}")
    public ResponseEntity<?> deletar(@PathVariable int id_conquista, @PathVariable int id_meta) {
        try {
            conquistaService.deletar(id_conquista, id_meta);
            logger.info("Conquista deletada com sucesso: id_conquista={}, id_meta={}", id_conquista, id_meta);
            return ResponseEntity.ok("Conquista deletada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar conquista com id_conquista = {} e id_meta = {}", id_conquista, id_meta, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar conquista");
        }
    }
}
