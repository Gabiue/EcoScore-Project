package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.Meta;
import com.ucbprojeto.ecoscore.service.MetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meta")
public class MetaController {

    @Autowired
    public MetaService metaService;

    private static final Logger logger = LoggerFactory.getLogger(MetaController.class);

    @GetMapping
    public ResponseEntity<List<Meta>> listarTodas() {
        try {
            List<Meta> metas = metaService.listarTodos();
            return ResponseEntity.ok(metas);
        } catch (Exception e) {
            logger.error("Erro ao listar metas", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meta> buscarPorId(@PathVariable int id) {
        try {
            Meta meta = metaService.buscarPorId(id);
            return ResponseEntity.ok(meta);
        } catch (Exception e) {
            logger.error("Erro ao buscar meta com id = {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/familia/{id_familia}")
    public ResponseEntity<List<Meta>> buscarPorFamilia(@PathVariable int id_familia) {
        try {
            List<Meta> metas = metaService.buscarPorFamilia(id_familia);
            return ResponseEntity.ok(metas);
        } catch (Exception e) {
            logger.error("Erro ao buscar metas da família com id = {}", id_familia, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Meta>> listarPorStatus(@PathVariable String status) {
        try {
            Meta.EStatus eStatus = Meta.EStatus.valueOf(status.toUpperCase());
            List<Meta> metas = metaService.listarPorStatus(eStatus);
            return ResponseEntity.ok(metas);
        } catch (IllegalArgumentException e) {
            logger.error("Status inválido: {}", status);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Erro ao listar metas por status = {}", status, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarMeta(@RequestBody Meta meta) {
        try {
            metaService.salvar(meta);
            logger.info("Meta criada com sucesso: {}", meta.getTitulo());
            return ResponseEntity.ok("Meta criada com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar meta: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar meta", e);
            return ResponseEntity.internalServerError().body("Erro ao criar meta");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Meta meta) {
        try {
            meta.setId_meta(id);
            metaService.atualizar(meta);
            logger.info("Meta atualizada com sucesso: id={}", id);
            return ResponseEntity.ok("Meta atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar meta com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar meta com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar meta");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable int id) {
        try {
            metaService.deletar(id);
            logger.info("Meta deletada com sucesso: id={}", id);
            return ResponseEntity.ok("Meta deletada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar meta com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar meta");
        }
    }
}