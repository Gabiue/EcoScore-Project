

-- 🧹 OBJETIVO: Manter o banco de dados limpo, organizado e funcionando bem
-- 📅 FREQUÊNCIA: Executar semanalmente/mensalmente conforme necessário

-- ==============================
-- 1. LIMPEZA DE DADOS ANTIGOS
-- ==============================

-- 1.1 - Limpar registros diários muito antigos (mais de 2 anos)
DELETE FROM registro_diario
WHERE data_registro < DATE_SUB(CURDATE(), INTERVAL 2 YEAR);

-- 1.2 - Limpar relatórios mensais antigos (mais de 3 anos)
DELETE FROM relatorio_mensal
WHERE mes_ano < DATE_SUB(CURDATE(), INTERVAL 3 YEAR);

-- 1.3 - Limpar conquistas de metas muito antigas
DELETE c FROM conquista c
JOIN meta m ON c.id_meta = m.id_meta
WHERE c.data_conquista < DATE_SUB(CURDATE(), INTERVAL 2 YEAR);

-- 1.4 - Limpar metas canceladas antigas (mais de 6 meses)
DELETE FROM meta
WHERE status = 'CANCELADA'
  AND data_fim < DATE_SUB(CURDATE(), INTERVAL 6 MONTH);

-- ==============================
-- 2. LIMPEZA DE DADOS INVÁLIDOS
-- ==============================

-- 2.1 - Remover telefones com números inválidos
DELETE FROM telefone
WHERE numero IS NULL
   OR numero = ''
   OR numero LIKE '%0000%'
   OR LENGTH(REPLACE(REPLACE(REPLACE(numero, '(', ''), ')', ''), '-', '')) < 10;

-- 2.2 - Remover registros com pontos zerados ou negativos
DELETE FROM registro_diario
WHERE pontos_ganhos <= 0 OR quantidade_realizada <= 0;

-- 2.3 - Remover associações família-meta inválidas
DELETE FROM familia_meta
WHERE progresso_individual < 0 OR progresso_individual > 100;

-- 2.4 - Remover conquistas com datas futuras (inválidas)
DELETE FROM conquista
WHERE data_conquista > CURDATE();

-- ==============================
-- 3. LIMPEZA DE DADOS ÓRFÃOS
-- ==============================

-- 3.1 - Remover telefones sem dono (membro foi deletado)
DELETE FROM telefone
WHERE cpf_membro NOT IN (SELECT cpf FROM membro);

-- 3.2 - Remover registros diários órfãos
DELETE FROM registro_diario
WHERE cpf_membro NOT IN (SELECT cpf FROM membro)
   OR id_pratica NOT IN (SELECT id_pratica FROM pratica_sustentavel);

-- 3.3 - Remover especializações órfãs
DELETE FROM membro_adulto
WHERE cpf NOT IN (SELECT cpf FROM membro);

DELETE FROM membro_crianca
WHERE cpf NOT IN (SELECT cpf FROM membro);

-- 3.4 - Remover associações família-meta órfãs
DELETE FROM familia_meta
WHERE id_familia NOT IN (SELECT id_familia FROM familia)
   OR id_meta NOT IN (SELECT id_meta FROM meta);

-- 3.5 - Remover relatórios de famílias que não existem mais
DELETE FROM relatorio_mensal
WHERE id_familia NOT IN (SELECT id_familia FROM familia);

-- ==============================
-- 4. LIMPEZA DE DADOS DUPLICADOS
-- ==============================

-- 4.1 - Remover telefones duplicados (mesmo membro, mesmo número)
DELETE t1 FROM telefone t1
INNER JOIN telefone t2
WHERE t1.id_telefone > t2.id_telefone
  AND t1.cpf_membro = t2.cpf_membro
  AND t1.numero = t2.numero;

-- 4.2 - Remover registros diários duplicados
DELETE rd1 FROM registro_diario rd1
INNER JOIN registro_diario rd2
WHERE rd1.id_registro > rd2.id_registro
  AND rd1.cpf_membro = rd2.cpf_membro
  AND rd1.id_pratica = rd2.id_pratica
  AND rd1.data_registro = rd2.data_registro;

-- 4.3 - Remover associações família-meta duplicadas
DELETE fm1 FROM familia_meta fm1
INNER JOIN familia_meta fm2
WHERE fm1.id_familia = fm2.id_familia
  AND fm1.id_meta = fm2.id_meta
  AND fm1.data_associacao > fm2.data_associacao;

-- ==============================
-- 5. CORREÇÃO DE INCONSISTÊNCIAS
-- ==============================

-- 5.1 - Corrigir supervisões inválidas (supervisor não é adulto)
UPDATE membro
SET cpf_supervisor = NULL
WHERE cpf_supervisor IS NOT NULL
  AND cpf_supervisor NOT IN (SELECT cpf FROM membro_adulto);

-- 5.2 - Corrigir crianças sem responsável
UPDATE membro_crianca mc
SET cpf_responsavel = (
    SELECT ma.cpf
    FROM membro_adulto ma
             JOIN membro m ON ma.cpf = m.cpf
    WHERE m.id_familia = (
        SELECT id_familia FROM membro WHERE cpf = mc.cpf
    ) AND ma.eh_responsavel = TRUE
    LIMIT 1
    )
WHERE cpf_responsavel NOT IN (SELECT cpf FROM membro_adulto);

-- 5.3 - Corrigir metas sem família criadora
UPDATE meta
SET criado_por_familia = NULL
WHERE criado_por_familia IS NOT NULL
  AND criado_por_familia NOT IN (SELECT id_familia FROM familia);

-- 5.4 - Corrigir progresso inválido (acima de 100%)
UPDATE familia_meta
SET progresso_individual = 100.00
WHERE progresso_individual > 100;

-- ==============================
-- 6. OTIMIZAÇÃO E REORGANIZAÇÃO
-- ==============================

-- 6.1 - Recriar estatísticas das tabelas
ANALYZE TABLE familia;
ANALYZE TABLE membro;
ANALYZE TABLE registro_diario;
ANALYZE TABLE meta;
ANALYZE TABLE familia_meta;

-- 6.2 - Otimizar tabelas fragmentadas
OPTIMIZE TABLE registro_diario;
OPTIMIZE TABLE telefone;
OPTIMIZE TABLE familia_meta;
OPTIMIZE TABLE relatorio_mensal;

-- 6.3 - Verificar e reparar tabelas se necessário
-- CHECK TABLE familia;
-- CHECK TABLE membro;
-- CHECK TABLE registro_diario;

-- ==============================
-- 7. RELATÓRIOS DE MANUTENÇÃO
-- ==============================

-- 7.1 - Verificar integridade dos dados após limpeza
SELECT 'VERIFICAÇÃO PÓS-LIMPEZA' AS titulo;

-- Contar registros órfãos restantes
SELECT 'Telefones órfãos' as problema, COUNT(*) as quantidade
FROM telefone
WHERE cpf_membro NOT IN (SELECT cpf FROM membro)

UNION ALL

SELECT 'Registros órfãos', COUNT(*)
FROM registro_diario
WHERE cpf_membro NOT IN (SELECT cpf FROM membro)

UNION ALL

SELECT 'Especializações órfãs', COUNT(*)
FROM membro_adulto
WHERE cpf NOT IN (SELECT cpf FROM membro)

UNION ALL

SELECT 'Supervisões inválidas', COUNT(*)
FROM membro
WHERE cpf_supervisor IS NOT NULL
  AND cpf_supervisor NOT IN (SELECT cpf FROM membro_adulto);

-- 7.2 - Relatório de espaço liberado
SELECT
    TABLE_NAME as tabela,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) as tamanho_mb
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE()
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;

-- 7.3 - Resumo de limpeza realizada
SELECT 'RESUMO DA MANUTENÇÃO' AS titulo;

SELECT
    (SELECT COUNT(*) FROM familia) AS familias_ativas,
    (SELECT COUNT(*) FROM membro) AS membros_totais,
    (SELECT COUNT(*) FROM registro_diario WHERE data_registro >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) AS registros_ultimos_30_dias,
    (SELECT COUNT(*) FROM meta WHERE status = 'ATIVA') AS metas_ativas,
    (SELECT COUNT(*) FROM familia_meta WHERE ativa = TRUE) AS participacoes_ativas;

-- ==============================
-- 8. PROCEDIMENTOS AUTOMATIZADOS
-- ==============================

-- 8.1 - Procedure para manutenção semanal
DELIMITER //
CREATE PROCEDURE ManutencaoSemanal()
BEGIN
    DECLARE registros_removidos INT DEFAULT 0;

    -- Log início
INSERT INTO log_manutencao (tipo, inicio, status)
VALUES ('SEMANAL', NOW(), 'INICIADO');

-- Limpeza de dados inválidos
DELETE FROM telefone WHERE numero IS NULL OR numero = '';
SET registros_removidos = ROW_COUNT();

DELETE FROM registro_diario WHERE pontos_ganhos <= 0;
SET registros_removidos = registros_removidos + ROW_COUNT();

    -- Limpeza de órfãos
DELETE FROM telefone WHERE cpf_membro NOT IN (SELECT cpf FROM membro);
SET registros_removidos = registros_removidos + ROW_COUNT();

    -- Otimização
    OPTIMIZE TABLE registro_diario;
    OPTIMIZE TABLE telefone;

    -- Log fim
UPDATE log_manutencao
SET fim = NOW(), status = 'CONCLUIDO', registros_afetados = registros_removidos
WHERE tipo = 'SEMANAL' AND DATE(inicio) = CURDATE();

SELECT CONCAT('Manutenção semanal concluída. ', registros_removidos, ' registros removidos.') AS resultado;
END //
DELIMITER ;

-- 8.2 - Procedure para manutenção mensal
DELIMITER //
CREATE PROCEDURE ManutencaoMensal()
BEGIN
    DECLARE registros_removidos INT DEFAULT 0;

    -- Limpeza de dados antigos
DELETE FROM registro_diario WHERE data_registro < DATE_SUB(CURDATE(), INTERVAL 2 YEAR);
SET registros_removidos = ROW_COUNT();

DELETE FROM relatorio_mensal WHERE mes_ano < DATE_SUB(CURDATE(), INTERVAL 3 YEAR);
SET registros_removidos = registros_removidos + ROW_COUNT();

    -- Limpeza completa de órfãos
DELETE FROM telefone WHERE cpf_membro NOT IN (SELECT cpf FROM membro);
DELETE FROM membro_adulto WHERE cpf NOT IN (SELECT cpf FROM membro);
DELETE FROM membro_crianca WHERE cpf NOT IN (SELECT cpf FROM membro);

-- Correção de inconsistências
UPDATE membro SET cpf_supervisor = NULL
WHERE cpf_supervisor NOT IN (SELECT cpf FROM membro_adulto);

-- Otimização completa
OPTIMIZE TABLE familia;
    OPTIMIZE TABLE membro;
    OPTIMIZE TABLE registro_diario;
    OPTIMIZE TABLE meta;

SELECT CONCAT('Manutenção mensal concluída. ', registros_removidos, ' registros antigos removidos.') AS resultado;
END //
DELIMITER ;

-- ==============================
-- 9. TABELA DE LOG PARA MANUTENÇÃO
-- ==============================

-- 9.1 - Criar tabela de log (se não existir)
CREATE TABLE IF NOT EXISTS log_manutencao (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              tipo ENUM('SEMANAL', 'MENSAL', 'MANUAL') NOT NULL,
    inicio DATETIME NOT NULL,
    fim DATETIME,
    status ENUM('INICIADO', 'CONCLUIDO', 'ERRO') DEFAULT 'INICIADO',
    registros_afetados INT DEFAULT 0,
    observacoes TEXT
    );

-- 9.2 - Registrar execução manual
INSERT INTO log_manutencao (tipo, inicio, status, observacoes)
VALUES ('MANUAL', NOW(), 'INICIADO', 'Manutenção executada manualmente');

-- ==============================
-- 10. COMANDOS DE USO PRÁTICO
-- ==============================

-- 10.1 - Executar manutenção semanal
-- CALL ManutencaoSemanal();

-- 10.2 - Executar manutenção mensal
-- CALL ManutencaoMensal();

-- 10.3 - Verificar logs de manutenção
-- SELECT * FROM log_manutencao ORDER BY inicio DESC LIMIT 10;

-- 10.4 - Verificar tamanho atual do banco
-- SELECT
--     ROUND(SUM(DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) AS tamanho_total_mb
-- FROM information_schema.TABLES
-- WHERE TABLE_SCHEMA = DATABASE();

