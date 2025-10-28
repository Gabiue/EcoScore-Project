package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.RelatorioMensal;
import com.ucbprojeto.ecoscore.service.RelatorioMensalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorio-mensal")
public class RelatorioMensalController {

    @Autowired
    public RelatorioMensalService relatorioMensalService;

    private static final Logger logger = LoggerFactory.getLogger(RelatorioMensalController.class);

    @GetMapping
    public ResponseEntity<List<RelatorioMensal>> listarTodos() {
        try {
            List<RelatorioMensal> relatorios = relatorioMensalService.listarTodos();
            return ResponseEntity.ok(relatorios);
        } catch (Exception e) {
            logger.error("Erro ao listar relatórios mensais", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioMensal> buscarPorId(@PathVariable int id) {
        try {
            RelatorioMensal relatorio = relatorioMensalService.buscarPorId(id);
            return ResponseEntity.ok(relatorio);
        } catch (Exception e) {
            logger.error("Erro ao buscar relatório mensal com id = {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/familia/{id_familia}")
    public ResponseEntity<List<RelatorioMensal>> buscarPorFamilia(@PathVariable int id_familia) {
        try {
            List<RelatorioMensal> relatorios = relatorioMensalService.buscarPorFamilia(id_familia);
            return ResponseEntity.ok(relatorios);
        } catch (Exception e) {
            logger.error("Erro ao buscar relatórios mensais da família com id = {}", id_familia, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarRelatorio(@RequestBody RelatorioMensal relatorioMensal) {
        try {
            relatorioMensalService.salvar(relatorioMensal);
            logger.info("Relatório mensal criado com sucesso: id_familia={}", relatorioMensal.getId_familia());
            return ResponseEntity.ok("Relatório mensal criado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar relatório mensal: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar relatório mensal", e);
            return ResponseEntity.internalServerError().body("Erro ao criar relatório mensal");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody RelatorioMensal relatorioMensal) {
        try {
            relatorioMensal.setId_relatorio(id);
            relatorioMensalService.atualizar(relatorioMensal);
            logger.info("Relatório mensal atualizado com sucesso: id={}", id);
            return ResponseEntity.ok("Relatório mensal atualizado com sucesso");
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar relatório mensal com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar relatório mensal com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar relatório mensal");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable int id) {
        try {
            relatorioMensalService.deletar(id);
            logger.info("Relatório mensal deletado com sucesso: id={}", id);
            return ResponseEntity.ok("Relatório mensal deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar relatório mensal com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar relatório mensal");
        }
    }
}