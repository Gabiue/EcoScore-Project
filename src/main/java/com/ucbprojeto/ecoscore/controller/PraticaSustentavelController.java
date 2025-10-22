package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.PraticaSustentavel;
import com.ucbprojeto.ecoscore.service.PraticaSustentavelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pratica-sustentavel")
public class PraticaSustentavelController {

    @Autowired
    public PraticaSustentavelService praticaSustentavelService;

    private static final Logger logger = LoggerFactory.getLogger(PraticaSustentavelController.class);

    @GetMapping
    public ResponseEntity<List<?>> listarTodos() {
        try{
            List<PraticaSustentavel> praticaSustentavel = praticaSustentavelService.ListarTodos();
            return ResponseEntity.ok(praticaSustentavel);
        }catch (Exception e){
            logger.error("Erro ao listar praticas sustentaveis",e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PraticaSustentavel> listarPorId(int id) {
        try{
            PraticaSustentavel praticaSustentavel = praticaSustentavelService.ListarPorId(id);
            return ResponseEntity.ok(praticaSustentavel);
        }catch (Exception e){
            logger.error ("Erro ao buscar pratica sustentavel com id = {}",id, e);
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<?> criarPraticaSustentavel(PraticaSustentavel praticaSustentavel){
        try{
            praticaSustentavelService.salvar(praticaSustentavel);
            logger.info("Pratica sustentavel criada com sucesso: {}", praticaSustentavel.getNome());
            return  ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            logger.error("Erro de validação ao criar pratica sustentavel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error("Erro ao criar pratica sustentavel", e);
            return ResponseEntity.internalServerError().body("Erro ao criar pratica sustentavel");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarPraticaSustentavel(@PathVariable int id, @RequestBody PraticaSustentavel praticaSustentavel){
        try{
            praticaSustentavel.setId_pratica(id);
            praticaSustentavelService.atualizar(praticaSustentavel);
            logger.info("Pratica sustentavel atualizada com sucesso: {}", praticaSustentavel.getNome());
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            logger.error("Erro de validação ao atualizar pratica sustentavel com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error("Erro ao atualizar pratica sustentavel com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar pratica sustentavel");}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPraticaSustentavel(@PathVariable int id){
        try{
            praticaSustentavelService.deletar(id);
            logger.info("Pratica sustentavel deletada com sucesso: id={}", id);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            logger.error("Erro de validação ao deletar pratica sustentavel com id = {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            logger.error("Erro ao deletar pratica sustentavel com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar pratica sustentavel");
        }
    }
}
