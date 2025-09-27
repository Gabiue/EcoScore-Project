package com.ucbprojeto.ecoscore.controller;

import com.ucbprojeto.ecoscore.dto.RegistroMembroDTO;
import com.ucbprojeto.ecoscore.model.Membro;
import com.ucbprojeto.ecoscore.service.MembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/membro")
public class MembroController {
    @Autowired
    private MembroService membroService;

    private static final Logger logger = LoggerFactory.getLogger(MembroController.class);

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroMembroDTO dto) {
        boolean sucesso = membroService.registrarMembro(dto);

        if (sucesso) {
            logger.info("Membro registrado com sucesso: CPF={}", dto.getCpf());
            return ResponseEntity.ok("Membro registrado com sucesso.");
        } else {
            logger.error("Erro ao registrar membro: CPF={}, DTO={}", dto.getCpf(), dto);
            return ResponseEntity.badRequest().body("Erro ao registrar membro. Verifique se a família existe, se o CPF não está duplicado e se todos os campos obrigatórios foram preenchidos.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Membro membro) {
        Membro autenticado = membroService.autenticarMembro(membro.getCpf(), membro.getSenha());
        if (autenticado != null) {
            return ResponseEntity.ok(autenticado);
        } else {
            return ResponseEntity.status(401).body("CPF ou senha inválidos.");
        }
    }

    @PutMapping("/{cpf}/familia/{id_familia}")
    public ResponseEntity<?> vincularMembroAFamilia(@PathVariable String cpf, @PathVariable int id_familia) {
        boolean sucesso = membroService.vincularMembroAFamilia(cpf, id_familia);
        if (sucesso) {
            logger.info("Membro vinculado à família com sucesso: CPF={}, id_familia={}", cpf, id_familia);
            return ResponseEntity.ok("Membro vinculado à família com sucesso.");
        } else {
            logger.error("Erro ao vincular membro à família: CPF={}, id_familia={}", cpf, id_familia);
            return ResponseEntity.badRequest().body("Erro ao vincular membro à família. Verifique se o membro e a família existem.");
        }
    }
}
