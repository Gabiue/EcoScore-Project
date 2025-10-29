
-- ==============================
-- DML MODIFICADO PARA O NOVO DDL
-- ==============================

-- Inserindo famílias
INSERT INTO familia (nome, rua, numero, bairro, cidade, cep, data_criacao)
VALUES ('Família Silva', 'Rua das Flores', '123', 'Centro', 'Brasília', '70000-000', '2025-01-10'),
       ('Família Santos', 'Av. Central', '456', 'Asa Norte', 'Brasília', '70001-000', '2025-01-15'),
       ('Família Costa', 'Rua Verde', '789', 'Taguatinga', 'Brasília', '70002-000', '2025-01-20');

-- Inserindo membros (SEM senha por enquanto - será adicionada depois)
INSERT INTO membro (cpf, senha, id_familia, nome, data_nascimento, papel_familia)
VALUES
-- Família Silva
('12345678901', 'senha_hash_joao', 1, 'João Silva', '1980-05-20', 'Pai'),
('98765432100', 'senha_hash_maria', 1, 'Maria Silva', '1982-07-15', 'Mãe'),
('11122233344', 'senha_hash_pedro', 1, 'Pedro Silva', '2010-02-12', 'Filho'),

-- Família Santos  
('22233344455', 'senha_hash_carlos', 2, 'Carlos Santos', '1975-03-10', 'Pai'),
('33344455566', 'senha_hash_ana', 2, 'Ana Santos', '1978-08-25', 'Mãe'),

-- Família Costa
('44455566677', 'senha_hash_luis', 3, 'Luis Costa', '1985-12-05', 'Pai'),
('55566677788', 'senha_hash_julia', 3, 'Julia Costa', '1987-09-30', 'Mãe'),
('66677788899', 'senha_hash_lucas', 3, 'Lucas Costa', '2012-04-18', 'Filho');

-- Definindo supervisão parental (crianças supervisionadas pelos pais)
UPDATE membro SET cpf_supervisor = '12345678901' WHERE cpf = '11122233344'; -- Pedro -> João
UPDATE membro SET cpf_supervisor = '44455566677' WHERE cpf = '66677788899'; -- Lucas -> Luis

-- Adultos e crianças (especializações)
INSERT INTO membro_adulto (cpf, eh_responsavel)
VALUES ('12345678901', TRUE),   -- João é responsável
       ('98765432100', FALSE),  -- Maria não é responsável principal
       ('22233344455', TRUE),   -- Carlos é responsável
       ('33344455566', FALSE),  -- Ana não é responsável principal
       ('44455566677', TRUE),   -- Luis é responsável
       ('55566677788', FALSE);  -- Julia não é responsável principal

INSERT INTO membro_crianca (cpf, cpf_responsavel, bonus_escolar)
VALUES ('11122233344', '12345678901', 50),  -- Pedro -> João
       ('66677788899', '44455566677', 75);  -- Lucas -> Luis

-- Telefones
INSERT INTO telefone (cpf_membro, numero)
VALUES ('12345678901', '(61)99999-1111'),
       ('98765432100', '(61)98888-2222'),
       ('22233344455', '(61)97777-3333'),
       ('33344455566', '(61)96666-4444'),
       ('44455566677', '(61)95555-5555'),
       ('55566677788', '(61)94444-6666');

-- Categorias de práticas
INSERT INTO categoria_pratica (nome, fator_multiplicador)
VALUES ('Energia', 1.5),
       ('Água', 1.2),
       ('Resíduos', 1.0),
       ('Transporte', 1.3);

-- ==============================
-- METAS (SEM id_familia - agora independentes)
-- ==============================
INSERT INTO meta (titulo, descricao, pontos_objetivo, data_inicio, data_fim, status, tipo, criado_por_familia)
VALUES
-- Meta individual da Família Silva
('Economizar energia', 'Reduzir consumo mensal em 10%', 200, '2025-02-01', '2025-03-01', 'ATIVA', 'INDIVIDUAL', 1),

-- Meta coletiva (várias famílias podem participar)
('Desafio Reciclagem', 'Separar 100% do lixo reciclável por 30 dias', 300, '2025-02-01', '2025-03-01', 'ATIVA', 'COLETIVA', 2),

-- Meta comunitária (aberta para todas as famílias)
('Plantar árvores no bairro', 'Meta comunitária: plantar 50 árvores', 500, '2025-03-01', '2025-05-31', 'ATIVA', 'COMUNITARIA', 3),

-- Meta individual da Família Santos
('Reduzir consumo de água', 'Economizar 20% na conta de água', 150, '2025-02-15', '2025-04-15', 'ATIVA', 'INDIVIDUAL', 2);

-- ==============================
-- FAMILIA_META (Relacionamento M:N)
-- ==============================
INSERT INTO familia_meta (id_familia, id_meta, data_associacao, progresso_individual, ativa, observacoes)
VALUES
-- Meta 1: Economizar energia (só Família Silva)
(1, 1, '2025-02-01', 25.50, TRUE, 'Progresso muito bom, já trocaram 5 lâmpadas'),

-- Meta 2: Desafio Reciclagem (Famílias Silva e Santos)
(1, 2, '2025-02-01', 60.00, TRUE, 'Separando tudo certinho'),
(2, 2, '2025-02-03', 45.00, TRUE, 'Começaram com atraso mas estão indo bem'),

-- Meta 3: Plantar árvores (todas as famílias participam)
(1, 3, '2025-03-01', 20.00, TRUE, 'Plantaram 10 mudas'),
(2, 3, '2025-03-02', 30.00, TRUE, 'Plantaram 15 mudas'),
(3, 3, '2025-03-01', 10.00, TRUE, 'Plantaram 5 mudas'),

-- Meta 4: Reduzir água (só Família Santos)
(2, 4, '2025-02-15', 15.00, TRUE, 'Instalaram reguladores de pressão');

-- Conquistas (entidade fraca de META)
INSERT INTO conquista (id_conquista, id_meta, data_conquista, pontos_bonus)
VALUES (1, 1, '2025-02-15', 50),    -- Conquista da meta de energia
       (1, 2, '2025-02-20', 30),    -- Conquista da meta de reciclagem
       (2, 2, '2025-02-25', 25),    -- Segunda conquista da reciclagem
       (1, 3, '2025-03-15', 40);    -- Conquista da meta comunitária


-- Relatórios mensais
INSERT INTO relatorio_mensal (id_familia, mes_ano, total_pontos_familia, total_praticas_realizadas)
VALUES (1, '2025-02-01', 115, 9),   -- Família Silva
       (2, '2025-02-01', 90, 6),    -- Família Santos  
       (3, '2025-02-01', 135, 3);   -- Família Costa