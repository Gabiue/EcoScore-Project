-- Inserindo famílias (sem responsável ainda)
INSERT INTO familia (nome, rua, numero, bairro, cidade, cep, data_criacao)
VALUES ('Família Silva', 'Rua das Flores', '123', 'Centro', 'Brasília', '70000-000', '2025-01-10');

-- Inserindo membros
INSERT INTO membro (cpf, id_familia, nome, data_nascimento, papel_familia)
VALUES ('12345678901', 1, 'João Silva', '1980-05-20', 'Pai'),
       ('98765432100', 1, 'Maria Silva', '1982-07-15', 'Mãe'),
       ('11122233344', 1, 'Pedro Silva', '2010-02-12', 'Filho');

-- Adultos e criança
INSERT INTO membro_adulto (cpf, email)
VALUES ('12345678901', 'joao@email.com'),
       ('98765432100', 'maria@email.com');

INSERT INTO membro_crianca (cpf, cpf_responsavel, bonus_escolar)
VALUES ('11122233344', '12345678901', 50);

-- 🔹 Agora atualizamos a família para definir o responsável
UPDATE familia
SET cpf_responsavel = '12345678901'
WHERE id_familia = 1;

-- Telefones
INSERT INTO telefone (cpf_membro, numero)
VALUES ('12345678901', '(61)99999-1111'),
       ('98765432100', '(61)98888-2222');

-- Categorias de práticas
INSERT INTO categoria_pratica (nome, fator_multiplicador)
VALUES ('Energia', 1.5), ('Água', 1.2), ('Resíduos', 1.0);

-- Práticas sustentáveis
INSERT INTO pratica_sustentavel (id_categoria, nome, descricao, pontos_base, dificuldade)
VALUES (1, 'Apagar luzes', 'Desligar luzes ao sair do cômodo', 10, 'FACIL'),
       (2, 'Reduzir banho', 'Banhos de no máximo 5 minutos', 20, 'MEDIA'),
       (3, 'Separar lixo reciclável', 'Separar papel, plástico e vidro', 15, 'FACIL');

-- Metas
INSERT INTO meta (id_familia, titulo, descricao, pontos_objetivo, data_inicio, data_fim, status)
VALUES (1, 'Economizar energia', 'Reduzir consumo mensal em 10%', 200, '2025-02-01', '2025-03-01', 'ATIVA');

-- Conquista
INSERT INTO conquista (id_conquista, id_meta, data_conquista, pontos_bonus)
VALUES (1, 1, '2025-02-15', 50);

-- Registro diário
INSERT INTO registro_diario (cpf_membro, id_pratica, data_registro, quantidade_realizada, pontos_ganhos)
VALUES ('12345678901', 1, '2025-02-10', 5, 50),
       ('98765432100', 2, '2025-02-11', 1, 20),
       ('11122233344', 3, '2025-02-12', 3, 45);

-- Relatório mensal
INSERT INTO relatorio_mensal (id_familia, mes_ano, total_pontos_familia, total_praticas_realizadas)
VALUES (1, '2025-02', 115, 9);
