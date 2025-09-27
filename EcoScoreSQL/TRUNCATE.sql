-- ⚠️ ATENÇÃO: TRUNCATE remove TODOS os dados das tabelas!
-- ⚠️ Execute com EXTREMO CUIDADO - não há como desfazer!
-- ⚠️ Recomenda-se fazer BACKUP antes de executar qualquer comando!

-- ==============================
-- 1. TRUNCATE DE TABELAS TRANSACIONAIS
-- ==============================

-- 1.1 - Limpar todos os registros diários
-- Remove todo o histórico de práticas realizadas
TRUNCATE TABLE registro_diario;

-- 1.2 - Limpar todos os telefones
-- Remove todos os telefones de todos os membros
TRUNCATE TABLE telefone;

-- 1.3 - Limpar todos os relatórios mensais
-- Remove todo o histórico de relatórios
TRUNCATE TABLE relatorio_mensal;

-- 1.4 - Limpar todas as conquistas
-- Remove todas as conquistas alcançadas
TRUNCATE TABLE conquista;

-- ==============================
-- 2. TRUNCATE DE TABELAS DE RELACIONAMENTO
-- ==============================

-- 2.1 - Limpar associações família-meta
-- Remove todas as participações de famílias em metas
TRUNCATE TABLE familia_meta;

-- ⚠️ ATENÇÃO: Após truncate família_meta, as metas ficarão sem participantes!

-- ==============================
-- 3. RESET COMPLETO DE DADOS TRANSACIONAIS
-- ==============================

-- 3.1 - Reset completo do sistema (manter estrutura)
-- Execute na ordem correta devido às foreign keys

-- Primeiro: tabelas dependentes
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE telefone;
TRUNCATE TABLE conquista;
TRUNCATE TABLE familia_meta;
TRUNCATE TABLE relatorio_mensal;

-- Depois: especializações de membro
TRUNCATE TABLE membro_crianca;
TRUNCATE TABLE membro_adulto;

-- Por último: tabelas principais
-- ⚠️ CUIDADO: Isso remove TODOS os dados!
-- TRUNCATE TABLE membro;
-- TRUNCATE TABLE meta;
-- TRUNCATE TABLE pratica_sustentavel;
-- TRUNCATE TABLE categoria_pratica;
-- TRUNCATE TABLE familia;

-- ==============================
-- 4. TRUNCATE SELETIVO POR MÓDULO
-- ==============================

-- 4.1 - Reset do módulo de práticas (manter famílias e metas)
TRUNCATE TABLE registro_diario;
-- Mantém: família, membro, meta, pratica_sustentavel

-- 4.2 - Reset do módulo de metas (manter famílias e práticas)
TRUNCATE TABLE conquista;
TRUNCATE TABLE familia_meta;
-- Opcional: TRUNCATE TABLE meta;

-- 4.3 - Reset do módulo de relatórios
TRUNCATE TABLE relatorio_mensal;

-- 4.4 - Reset do módulo de comunicação
TRUNCATE TABLE telefone;

-- ==============================
-- 5. CENÁRIOS DE LIMPEZA ESPECÍFICOS
-- ==============================

-- 5.1 - Preparar sistema para novo período (manter estrutura)
-- Limpa dados transacionais mas mantém configurações
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE conquista;
TRUNCATE TABLE relatorio_mensal;
-- Zerar progresso das metas (via UPDATE, não TRUNCATE)
-- UPDATE familia_meta SET progresso_individual = 0.00;

-- 5.2 - Reset para ambiente de testes
-- Remove todos os dados, mantém apenas estrutura
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE telefone;
TRUNCATE TABLE conquista;
TRUNCATE TABLE familia_meta;
TRUNCATE TABLE relatorio_mensal;
TRUNCATE TABLE membro_crianca;
TRUNCATE TABLE membro_adulto;
-- TRUNCATE TABLE membro;        -- Descomente se necessário
-- TRUNCATE TABLE meta;          -- Descomente se necessário
-- TRUNCATE TABLE familia;       -- Descomente se necessário

-- 5.3 - Reset para demo/apresentação
-- Mantém estrutura básica, remove apenas dados variáveis
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE conquista;
TRUNCATE TABLE relatorio_mensal;
TRUNCATE TABLE telefone;

-- ==============================
-- 6. SCRIPTS DE RESET COM VERIFICAÇÕES
-- ==============================

-- 6.1 - Reset seguro com contagem prévia
-- Verificar quantos registros serão perdidos
SELECT 'registro_diario' as tabela, COUNT(*) as registros FROM registro_diario
UNION ALL
SELECT 'telefone', COUNT(*) FROM telefone
UNION ALL
SELECT 'conquista', COUNT(*) FROM conquista
UNION ALL
SELECT 'familia_meta', COUNT(*) FROM familia_meta
UNION ALL
SELECT 'relatorio_mensal', COUNT(*) FROM relatorio_mensal;

-- Depois de verificar, execute o truncate:
-- TRUNCATE TABLE registro_diario;
-- TRUNCATE TABLE telefone;
-- TRUNCATE TABLE conquista;
-- TRUNCATE TABLE familia_meta;
-- TRUNCATE TABLE relatorio_mensal;

-- ==============================
-- 7. PROCEDIMENTOS DE RESET AUTOMATIZADO
-- ==============================

-- 7.1 - Procedure para reset mensal
DELIMITER //
CREATE PROCEDURE ResetMensal()
BEGIN
    -- Fazer backup dos dados importantes primeiro
CREATE TABLE backup_relatorio_mensal AS SELECT * FROM relatorio_mensal;

-- Limpar dados do mês anterior
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE telefone;

-- Reset progresso das metas
UPDATE familia_meta SET progresso_individual = 0.00 WHERE ativa = TRUE;

SELECT 'Reset mensal concluído!' as status;
END //
DELIMITER ;

-- Usar: CALL ResetMensal();

-- 7.2 - Procedure para reset anual
DELIMITER //
CREATE PROCEDURE ResetAnual()
BEGIN
    -- Backup completo
CREATE TABLE backup_registro_diario AS SELECT * FROM registro_diario;
CREATE TABLE backup_conquista AS SELECT * FROM conquista;
CREATE TABLE backup_relatorio_mensal AS SELECT * FROM relatorio_mensal;

-- Reset completo dos dados transacionais
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE conquista;
TRUNCATE TABLE relatorio_mensal;
TRUNCATE TABLE familia_meta;

-- Reset bonus das crianças
UPDATE membro_crianca SET bonus_escolar = 0;

SELECT 'Reset anual concluído!' as status;
END //
DELIMITER ;

-- Usar: CALL ResetAnual();

-- ==============================
-- 8. TRUNCATE PARA MIGRAÇÃO DE DADOS
-- ==============================

-- 8.1 - Preparar para importação de novos dados
-- Limpa tudo exceto configurações básicas
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE telefone;
TRUNCATE TABLE conquista;
TRUNCATE TABLE familia_meta;
TRUNCATE TABLE relatorio_mensal;
TRUNCATE TABLE membro_crianca;
TRUNCATE TABLE membro_adulto;
TRUNCATE TABLE membro;

-- Agora pode importar novos dados via INSERT

-- 8.2 - Reset para nova versão do sistema
-- Remove dados incompatíveis com nova versão
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE conquista;
TRUNCATE TABLE relatorio_mensal;

-- ==============================
-- 9. SCRIPTS DE VERIFICAÇÃO PÓS-TRUNCATE
-- ==============================

-- 9.1 - Verificar se tabelas estão vazias
SELECT
    'registro_diario' as tabela,
    COUNT(*) as registros_restantes
FROM registro_diario
UNION ALL
SELECT 'telefone', COUNT(*) FROM telefone
UNION ALL
SELECT 'conquista', COUNT(*) FROM conquista
UNION ALL
SELECT 'familia_meta', COUNT(*) FROM familia_meta
UNION ALL
SELECT 'relatorio_mensal', COUNT(*) FROM relatorio_mensal;

-- 9.2 - Verificar integridade após reset
SELECT
    f.nome,
    COUNT(m.cpf) as membros,
    COUNT(t.id_telefone) as telefones,
    COUNT(fm.id_meta) as metas_ativas
FROM familia f
         LEFT JOIN membro m ON f.id_familia = m.id_familia
         LEFT JOIN telefone t ON m.cpf = t.cpf_membro
         LEFT JOIN familia_meta fm ON f.id_familia = fm.id_familia AND fm.ativa = TRUE
GROUP BY f.id_familia, f.nome;

-- ==============================
-- 10. COMANDOS DE EMERGÊNCIA
-- ==============================

-- 10.1 - Reset total em caso de corrupção de dados
-- ⚠️ ÚLTIMO RECURSO - REMOVE TODOS OS DADOS!
/*
TRUNCATE TABLE registro_diario;
TRUNCATE TABLE telefone;
TRUNCATE TABLE conquista;
TRUNCATE TABLE familia_meta;
TRUNCATE TABLE relatorio_mensal;
TRUNCATE TABLE membro_crianca;
TRUNCATE TABLE membro_adulto;
TRUNCATE TABLE membro;
TRUNCATE TABLE meta;
TRUNCATE TABLE pratica_sustentavel;
TRUNCATE TABLE categoria_pratica;
TRUNCATE TABLE familia;
*/

-- 10.2 - Recriar dados básicos após reset total
-- Execute apenas após TRUNCATE total
/*
INSERT INTO categoria_pratica (nome, fator_multiplicador) VALUES
('Energia', 1.5),
('Água', 1.2),
('Resíduos', 1.0);

INSERT INTO pratica_sustentavel (id_categoria, nome, pontos_base, dificuldade) VALUES
(1, 'Apagar luzes', 10, 'FACIL'),
(2, 'Banho rápido', 20, 'MEDIA'),
(3, 'Separar lixo', 15, 'FACIL');
*/