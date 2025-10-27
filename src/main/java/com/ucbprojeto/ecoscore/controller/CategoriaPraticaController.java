package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.model.CategoriaPratica;
import com.ucbprojeto.ecoscore.service.CategoriaPraticaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria-pratica")
public class CategoriaPraticaController {

    @Autowired
    public CategoriaPraticaService categoriaPraticaService;

    private static final Logger logger = LoggerFactory.getLogger(CategoriaPraticaController.class);


    @GetMapping
    public ResponseEntity<List<CategoriaPratica>> listarTodas(){
        try{
            List<CategoriaPratica> categorias = categoriaPraticaService.listarTodos();
            return ResponseEntity.ok(categorias);
        }catch (Exception e){
            logger.error("Erro ao listar categorias de pratica", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPratica> buscarPorId(@PathVariable Integer id){
        try{
            CategoriaPratica categoriaPratica = categoriaPraticaService.buscarPorId(id);
            return ResponseEntity.ok(categoriaPratica);
        }catch(Exception e){
            logger.error("Erro ao listar categorias de pratica", e);
            return ResponseEntity.notFound().build();
        }
    }

    // Adicionar CategoriaPratica
    @PostMapping
    public ResponseEntity<?> criarCategoriaPratica(@RequestBody CategoriaPratica categoriaPratica){
        try{
            categoriaPraticaService.salvar(categoriaPratica);
            logger.info("Categoria de Pratica criada com sucesso: {}", categoriaPratica.getNome());
            return ResponseEntity.ok("Categoria de Pratica criada com sucesso");
        }catch(Exception e) {
            logger.error("Erro ao criar categoria de pratica", e);
            return ResponseEntity.internalServerError().body("Erro ao criar categoria de pratica");
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody CategoriaPratica categoriaPratica) {
        try {
            categoriaPratica.setId_categoria(id);
            categoriaPraticaService.atualizar(categoriaPratica);
            logger.info("Categoria de Pratica atualizada com sucesso: id={}", id);
            return ResponseEntity.ok("Categoria de Pratica atualizada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar categoria de pratica com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar categoria de pratica");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id){
        try{
            categoriaPraticaService.deletar(id);
            logger.info("Categoria de Pratica deletada com sucesso: id={}", id);
            return ResponseEntity.ok("Categoria de Pratica deletada com sucesso");
        }catch(Exception e){
            logger.error("Erro ao deletar categoria de pratica com id = {}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao deletar categoria de pratica");
        }
    }
}
