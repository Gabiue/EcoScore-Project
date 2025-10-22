-- ==============================
-- 1. UPDATES DE FAMÍLIA
-- ==============================

-- 1.1 - Atualizar endereço completo da família
UPDATE familia
SET rua = 'Rua Nova das Palmeiras',
    numero = '456',
    bairro = 'Jardim América',
    cep = '70001-456'
WHERE id_familia = 1;

-- 1.2 - Atualizar apenas o nome da família
UPDATE familia
SET nome = 'Família Silva Santos'
WHERE id_familia = 1;

-- 1.3 - Atualizar cidade de todas as famílias
UPDATE familia
SET cidade = 'Distrito Federal'
WHERE cidade = 'Brasília';

-- ==============================
-- 2. UPDATES DE MEMBRO
-- ==============================

-- 2.1 - Atualizar senha de um membro
UPDATE membro
SET senha = 'nova_senha_hash_criptografada_456'
WHERE cpf = '12345678901';

-- 2.2 - Alterar papel do membro na família
UPDATE membro
SET papel_familia = 'Padrasto'
WHERE cpf = '12345678901';

-- 2.3 - Transferir membro para outra família
UPDATE membro
SET id_familia = 2
WHERE cpf = '11122233344';

-- 2.4 - Definir novo supervisor para criança
UPDATE membro
SET cpf_supervisor = '98765432100'
WHERE cpf = '11122233344';

-- 2.5 - Remover supervisor (tornar independente)
UPDATE membro
SET cpf_supervisor = NULL
WHERE cpf = '11122233344';

-- 2.6 - Atualizar dados pessoais de membro
UPDATE membro
SET nome = 'João da Silva Santos',
    papel_familia = 'Pai'
WHERE cpf = '12345678901';

-- ==============================
-- 3. UPDATES DE MEMBRO ADULTO
-- ==============================

-- 3.1 - Tornar membro adulto responsável
UPDATE membro_adulto
SET eh_responsavel = TRUE
WHERE cpf = '98765432100';

-- 3.2 - Remover responsabilidade
UPDATE membro_adulto
SET eh_responsavel = FALSE
WHERE cpf = '12345678901';

-- 3.3 - Alternar responsabilidade entre adultos
UPDATE membro_adulto
SET eh_responsavel = CASE
                         WHEN cpf = '12345678901' THEN FALSE
                         WHEN cpf = '98765432100' THEN TRUE
                         ELSE eh_responsavel
    END
WHERE cpf IN ('12345678901', '98765432100');

-- ==============================
-- 4. UPDATES DE MEMBRO CRIANÇA
-- ==============================

-- 4.1 - Adicionar bonus escolar
UPDATE membro_crianca
SET bonus_escolar = bonus_escolar + 25
WHERE cpf = '11122233344';

-- 4.2 - Definir bonus escolar específico
UPDATE membro_crianca
SET bonus_escolar = 100
WHERE cpf = '11122233344';

-- 4.3 - Zerar bonus escolar (nova temporada)
UPDATE membro_crianca
SET bonus_escolar = 0
WHERE cpf = '11122233344';

-- 4.4 - Trocar responsável da criança
UPDATE membro_crianca
SET cpf_responsavel = '98765432100'
WHERE cpf = '11122233344';

-- 4.5 - Aplicar bonus para todas as crianças da família
UPDATE membro_crianca mc
    JOIN membro m ON mc.cpf = m.cpf
    SET mc.bonus_escolar = mc.bonus_escolar + 50
WHERE m.id_familia = 1;

-- ==============================
-- 5. UPDATES DE TELEFONE
-- ==============================

-- 5.1 - Atualizar número de telefone específico
UPDATE telefone
SET numero = '(61)99999-8888'
WHERE id_telefone = 1;

-- 5.2 - Atualizar DDD de todos os telefones de um membro
UPDATE telefone
SET numero = REPLACE(numero, '(61)', '(11)')
WHERE cpf_membro = '12345678901';

-- 5.3 - Padronizar formato de telefones
UPDATE telefone
SET numero = CONCAT('(61)', SUBSTRING(numero, -9))
WHERE numero NOT LIKE '(61)%' AND cpf_membro = '12345678901';

-- ==============================
-- 6. UPDATES DE CATEGORIA PRÁTICA
-- ==============================

-- 6.1 - Ajustar fator multiplicador de categoria
UPDATE categoria_pratica
SET fator_multiplicador = 2.0
WHERE id_categoria = 1;

-- 6.2 - Renomear categoria
UPDATE categoria_pratica
SET nome = 'Economia de Energia'
WHERE id_categoria = 1;

-- 6.3 - Reajuste geral de fatores (aumento de 10%)
UPDATE categoria_pratica
SET fator_multiplicador = fator_multiplicador * 1.1;

-- ==============================
-- 7. UPDATES DE PRÁTICA SUSTENTÁVEL
-- ==============================

-- 7.1 - Atualizar pontos base de uma prática
UPDATE pratica_sustentavel
SET pontos_base = 25
WHERE id_pratica = 1;

-- 7.2 - Alterar dificuldade de prática
UPDATE pratica_sustentavel
SET dificuldade = 'MEDIA'
WHERE id_pratica = 1;

-- 7.3 - Atualizar descrição e frequência
UPDATE pratica_sustentavel
SET descricao = 'Desligar todas as luzes ao sair do ambiente - Meta 2025',
    frequencia_esperada = 'Diária'
WHERE id_pratica = 1;

-- 7.4 - Mover prática para outra categoria
UPDATE pratica_sustentavel
SET id_categoria = 2
WHERE id_pratica = 1;

-- 7.5 - Reajustar pontos de práticas difíceis
UPDATE pratica_sustentavel
SET pontos_base = pontos_base + 10
WHERE dificuldade = 'DIFICIL';

-- ==============================
-- 8. UPDATES DE META
-- ==============================

-- 8.1 - Marcar meta como concluída
UPDATE meta
SET status = 'CONCLUIDA'
WHERE id_meta = 1;

-- 8.2 - Cancelar meta
UPDATE meta
SET status = 'CANCELADA'
WHERE id_meta = 2;

-- 8.3 - Reativar meta cancelada
UPDATE meta
SET status = 'ATIVA'
WHERE id_meta = 2;

-- 8.4 - Estender prazo da meta
UPDATE meta
SET data_fim = DATE_ADD(data_fim, INTERVAL 30 DAY)
WHERE id_meta = 1;

-- 8.5 - Aumentar objetivo de pontos
UPDATE meta
SET pontos_objetivo = 500,
    descricao = CONCAT(descricao, ' - Objetivo aumentado para maior desafio!')
WHERE id_meta = 1;

-- 8.6 - Alterar tipo de meta
UPDATE meta
SET tipo = 'COMUNITARIA'
WHERE id_meta = 1;

-- 8.7 - Atualizar família criadora
UPDATE meta
SET criado_por_familia = 2
WHERE id_meta = 1;

-- 8.8 - Atualizar metas vencidas automaticamente
UPDATE meta
SET status = 'CANCELADA'
WHERE data_fim < CURDATE() AND status = 'ATIVA';

-- ==============================
-- 9. UPDATES DE FAMÍLIA_META
-- ==============================

-- 9.1 - Atualizar progresso individual
UPDATE familia_meta
SET progresso_individual = 75.5,
    observacoes = 'Progresso excelente! Meta quase concluída.'
WHERE id_familia = 1 AND id_meta = 1;

-- 9.2 - Desativar participação na meta
UPDATE familia_meta
SET ativa = FALSE,
    observacoes = 'Família optou por focar em outras metas prioritárias.'
WHERE id_familia = 1 AND id_meta = 2;

-- 9.3 - Reativar participação
UPDATE familia_meta
SET ativa = TRUE,
    observacoes = 'Família retomou participação após reorganização.'
WHERE id_familia = 1 AND id_meta = 2;

-- 9.4 - Zerar progresso (reiniciar meta)
UPDATE familia_meta
SET progresso_individual = 0.00,
    data_associacao = CURDATE(),
    observacoes = 'Progresso reiniciado conforme solicitado.'
WHERE id_familia = 1 AND id_meta = 1;

-- 9.5 - Atualizar data de associação
UPDATE familia_meta
SET data_associacao = '2025-03-01'
WHERE id_familia = 1 AND id_meta = 1;

-- 9.6 - Atualizar progresso baseado em pontos ganhos
UPDATE familia_meta fm
    JOIN (
    SELECT
    fm.id_familia,
    fm.id_meta,
    LEAST(100, (SUM(rd.pontos_ganhos) / m.pontos_objetivo) * 100) AS novo_progresso
    FROM familia_meta fm
    JOIN meta m ON fm.id_meta = m.id_meta
    JOIN membro mb ON mb.id_familia = fm.id_familia
    JOIN registro_diario rd ON mb.cpf = rd.cpf_membro
    WHERE rd.data_registro >= m.data_inicio
    GROUP BY fm.id_familia, fm.id_meta
    ) calc ON fm.id_familia = calc.id_familia AND fm.id_meta = calc.id_meta
    SET fm.progresso_individual = calc.novo_progresso
WHERE fm.ativa = TRUE;

-- ==============================
-- 10. UPDATES DE REGISTRO DIÁRIO
-- ==============================

-- 10.1 - Corrigir quantidade realizada
UPDATE registro_diario
SET quantidade_realizada = 5,
    pontos_ganhos = 50
WHERE id_registro = 1;

-- 10.2 - Corrigir data de registro
UPDATE registro_diario
SET data_registro = '2025-02-15'
WHERE id_registro = 1;

-- 10.3 - Recalcular pontos baseado na fórmula
UPDATE registro_diario rd
    JOIN pratica_sustentavel ps ON rd.id_pratica = ps.id_pratica
    JOIN categoria_pratica cp ON ps.id_categoria = cp.id_categoria
    SET rd.pontos_ganhos = (ps.pontos_base * rd.quantidade_realizada * cp.fator_multiplicador)
WHERE rd.id_registro = 1;

-- 10.4 - Recalcular todos os pontos do sistema
UPDATE registro_diario rd
    JOIN pratica_sustentavel ps ON rd.id_pratica = ps.id_pratica
    JOIN categoria_pratica cp ON ps.id_categoria = cp.id_categoria
    SET rd.pontos_ganhos = (ps.pontos_base * rd.quantidade_realizada * cp.fator_multiplicador);

-- ==============================
-- 11. UPDATES DE CONQUISTA
-- ==============================

-- 11.1 - Atualizar pontos bonus de conquista
UPDATE conquista
SET pontos_bonus = 100
WHERE id_conquista = 1 AND id_meta = 1;

-- 11.2 - Corrigir data de conquista
UPDATE conquista
SET data_conquista = '2025-02-20'
WHERE id_conquista = 1 AND id_meta = 1;

-- 11.3 - Aplicar bonus geral em todas as conquistas
UPDATE conquista
SET pontos_bonus = pontos_bonus + 25;

-- ==============================
-- 12. UPDATES DE RELATÓRIO MENSAL
-- ==============================

-- 12.1 - Atualizar totais do relatório
UPDATE relatorio_mensal
SET total_pontos_familia = 1500,
    total_praticas_realizadas = 50
WHERE id_relatorio = 1;

-- 12.2 - Corrigir mês/ano do relatório
UPDATE relatorio_mensal
SET mes_ano = '2025-03-01'
WHERE id_relatorio = 1;

-- 12.3 - Recalcular relatório atual automaticamente
UPDATE relatorio_mensal rm
    JOIN (
    SELECT
    f.id_familia,
    COALESCE(SUM(rd.pontos_ganhos), 0) AS pontos_calculados,
    COUNT(DISTINCT rd.id_registro) AS praticas_calculadas
    FROM familia f
    LEFT JOIN membro m ON f.id_familia = m.id_familia
    LEFT JOIN registro_diario rd ON m.cpf = rd.cpf_membro
    AND MONTH(rd.data_registro) = MONTH(CURDATE())
    AND YEAR(rd.data_registro) = YEAR(CURDATE())
    GROUP BY f.id_familia
    ) calc ON rm.id_familia = calc.id_familia
    SET rm.total_pontos_familia = calc.pontos_calculados,
        rm.total_praticas_realizadas = calc.praticas_calculadas
WHERE rm.mes_ano = DATE_FORMAT(CURDATE(), '%Y-%m-01');

-- ==============================
-- 13. UPDATES CONDICIONAIS AVANÇADOS
-- ==============================

-- 13.1 - Migrar criança para adulto ao completar 18 anos
-- Primeiro, inserir na tabela membro_adulto
INSERT INTO membro_adulto (cpf, eh_responsavel)
SELECT cpf, FALSE
FROM membro
WHERE YEAR(CURDATE()) - YEAR(data_nascimento) >= 18
  AND cpf IN (SELECT cpf FROM membro_crianca)
  AND cpf NOT IN (SELECT cpf FROM membro_adulto);

-- Depois, remover da tabela membro_crianca
DELETE FROM membro_crianca
WHERE cpf IN (
    SELECT cpf FROM membro
    WHERE YEAR(CURDATE()) - YEAR(data_nascimento) >= 18
    );

-- 13.2 - Promover membro para responsável se for o único adulto
UPDATE membro_adulto ma
SET eh_responsavel = TRUE
WHERE ma.cpf IN (
    SELECT m.cpf FROM membro m
    WHERE m.id_familia IN (
        SELECT f.id_familia FROM familia f
        WHERE f.id_familia NOT IN (
            SELECT DISTINCT m2.id_familia
            FROM membro m2
                     JOIN membro_adulto ma2 ON m2.cpf = ma2.cpf
            WHERE ma2.eh_responsavel = TRUE
        )
    )
    LIMIT 1
    );

-- 13.3 - Ajustar dificuldade baseado na performance
UPDATE pratica_sustentavel ps
SET dificuldade = CASE
                      WHEN (
                               SELECT AVG(rd.quantidade_realizada)
                               FROM registro_diario rd
                               WHERE rd.id_pratica = ps.id_pratica
                           ) > 5 THEN 'FACIL'
                      WHEN (
                               SELECT AVG(rd.quantidade_realizada)
                               FROM registro_diario rd
                               WHERE rd.id_pratica = ps.id_pratica
                           ) > 2 THEN 'MEDIA'
                      ELSE 'DIFICIL'
    END
WHERE ps.id_pratica IN (
    SELECT DISTINCT id_pratica FROM registro_diario
);

-- 13.4 - Equalizar progresso de metas comunitárias
UPDATE familia_meta fm
    JOIN (
    SELECT
    id_meta,
    AVG(progresso_individual) as progresso_medio
    FROM familia_meta
    WHERE ativa = TRUE
    GROUP BY id_meta
    ) avg_progress ON fm.id_meta = avg_progress.id_meta
    JOIN meta m ON fm.id_meta = m.id_meta
    SET fm.progresso_individual = avg_progress.progresso_medio
WHERE m.tipo = 'COMUNITARIA' AND fm.ativa = TRUE;

-- ==============================
-- 14. UPDATES DE MANUTENÇÃO AUTOMÁTICA
-- ==============================

-- 14.1 - Atualizar status de metas baseado na data
UPDATE meta
SET status = CASE
                 WHEN data_fim < CURDATE() AND status = 'ATIVA' THEN 'CANCELADA'
                 WHEN data_inicio > CURDATE() AND status = 'ATIVA' THEN 'ATIVA'
                 ELSE status
    END;

-- 14.2 - Aplicar bonus sazonal para crianças
UPDATE membro_crianca
SET bonus_escolar = bonus_escolar +
                    CASE MONTH(CURDATE())
    WHEN 12 THEN 100  -- Dezembro: bonus natalino
    WHEN 6 THEN 50    -- Junho: bonus meio do ano
    WHEN 3 THEN 25    -- Março: bonus trimestral
    ELSE 0
END
WHERE bonus_escolar < 500;  -- Limite máximo

-- 14.3 - Reajustar fatores multiplicadores anualmente
UPDATE categoria_pratica
SET fator_multiplicador = fator_multiplicador * 1.05  -- Aumento de 5%
WHERE MONTH(CURDATE()) = 1 AND DAY(CURDATE()) = 1;  -- 1º de Janeiro

-- 14.4 - Padronizar formato de telefones
UPDATE telefone
SET numero = CONCAT(
        '(',
        SUBSTRING(REPLACE(REPLACE(REPLACE(numero, '(', ''), ')', ''), '-', ''), 1, 2),
        ')',
        SUBSTRING(REPLACE(REPLACE(REPLACE(numero, '(', ''), ')', ''), '-', ''), 3, 5),
        '-',
        SUBSTRING(REPLACE(REPLACE(REPLACE(numero, '(', ''), ')', ''), '-', ''), 8, 4)
             )
WHERE numero NOT REGEXP '^\\([0-9]{2}\\)[0-9]{5}-[0-9]{4};

-- ==============================
-- 15. UPDATES EM LOTE
-- ==============================

-- 15.1 - Atualizar todas as metas vencidas
UPDATE meta
SET status = 'CANCELADA'
WHERE data_fim < CURDATE() AND status = 'ATIVA';

-- 15.2 - Reativar todas as associações
UPDATE familia_meta
SET ativa = TRUE
WHERE ativa = FALSE;

-- 15.3 - Aumentar pontos de práticas difíceis
UPDATE pratica_sustentavel
SET pontos_base = pontos_base + 15
WHERE dificuldade = 'DIFICIL';

-- 15.4 - Aplicar reajuste geral em categorias
UPDATE categoria_pratica
SET fator_multiplicador = ROUND(fator_multiplicador * 1.1, 2);

-- 15.5 - Corrigir supervisões inválidas
UPDATE membro
SET cpf_supervisor = (
    SELECT ma.cpf FROM membro_adulto ma
    JOIN membro m2 ON ma.cpf = m2.cpf
    WHERE m2.id_familia = membro.id_familia
      AND ma.eh_responsavel = TRUE
    LIMIT 1
)
WHERE cpf IN (SELECT cpf FROM membro_crianca)
  AND (cpf_supervisor IS NULL OR cpf_supervisor NOT IN (SELECT cpf FROM membro_adulto));