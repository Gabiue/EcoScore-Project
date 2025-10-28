package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.Familia;
import com.ucbprojeto.ecoscore.service.FamiliaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/familia")
public class FamiliaController {

    @Autowired
    public FamiliaService familiaService;

    private static final Logger logger = LoggerFactory.getLogger(FamiliaController.class);

    @GetMapping
    public ResponseEntity<List<Familia>> listarTodas(){
        try{
            List<Familia> familias = familiaService.listar();
            return ResponseEntity.ok(familias);
        }catch (Exception e){
            logger.error("Erro ao listar familias", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Familia> buscarPorId(@PathVariable int id){
        try {
            Familia familia = familiaService.buscarPorId(id);
            return ResponseEntity.ok(familia);
        } catch (Exception e) {
            logger.error("Erro ao buscar familia com id = {}",id, e);
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<?> criarFamilia(@RequestBody Familia familia){
        try {
            familiaService.salvar(familia);
            logger.info("Familia criada com sucesso: {}", familia.getNome());
            return ResponseEntity.ok("Familia criada com sucesso");
        }catch(IllegalArgumentException e){
            logger.error("Erro de validação ao criar familia: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            logger.error("Error ao criar familia", e);
            return ResponseEntity.internalServerError().body("Erro ao criar familia");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Familia familia){
        try{
            familia.setId_familia(id);
            familiaService.atualizar(familia);
            logger.info("Familia atualizada com sucesso: id={}", id);
            return ResponseEntity.ok("Familia atualizada com sucesso");
        }catch(IllegalArgumentException e){
            logger.error("Erro de validação ao atualizar familia com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error("Erro ao atualizar familia com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar familia");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable int id){
        try {
            familiaService.deletar(id);
            logger.info("Família deletada com sucesso: id={}", id);
            return ResponseEntity.ok("Família deletada com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao deletar família com id={}: ", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar família.");
        }

    }

}
