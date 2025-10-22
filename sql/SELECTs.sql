-- ==============================
-- 1. CONSULTAS BÁSICAS DE FAMÍLIA
-- ==============================

-- 1.1 - Listar todas as famílias
SELECT
    id_familia,
    nome,
    CONCAT(rua, ', ', numero, ' - ', bairro, ', ', cidade) AS endereco_completo,
    cep,
    data_criacao
FROM familia
ORDER BY nome;

-- 1.2 - Buscar família por ID
SELECT * FROM familia WHERE id_familia = 1;

-- 1.3 - Contar total de famílias por cidade
SELECT
    cidade,
    COUNT(*) as total_familias
FROM familia
GROUP BY cidade
ORDER BY total_familias DESC;

-- ==============================
-- 2. CONSULTAS DE MEMBROS
-- ==============================

-- 2.1 - Listar todos os membros de uma família
SELECT
    m.nome,
    m.cpf,
    m.papel_familia,
    YEAR(CURDATE()) - YEAR(m.data_nascimento) AS idade,
    CASE
    WHEN ma.cpf IS NOT NULL THEN 'Adulto'
    WHEN mc.cpf IS NOT NULL THEN 'Criança'
    ELSE 'Indefinido'
END AS tipo_membro
FROM membro m
LEFT JOIN membro_adulto ma ON m.cpf = ma.cpf
LEFT JOIN membro_crianca mc ON m.cpf = mc.cpf
WHERE m.id_familia = 1
ORDER BY m.papel_familia;

-- 2.2 - Buscar membro por CPF
SELECT
    m.*,
    f.nome AS nome_familia
FROM membro m
         JOIN familia f ON m.id_familia = f.id_familia
WHERE m.cpf = '12345678901';

-- 2.3 - Listar responsáveis e suas crianças
SELECT
    resp.nome AS responsavel,
    resp.cpf AS cpf_responsavel,
    crianca.nome AS crianca,
    crianca.cpf AS cpf_crianca,
    mc.bonus_escolar,
    f.nome AS familia
FROM membro resp
         JOIN membro_adulto ma ON resp.cpf = ma.cpf
         JOIN membro_crianca mc ON resp.cpf = mc.cpf_responsavel
         JOIN membro crianca ON mc.cpf = crianca.cpf
         JOIN familia f ON resp.id_familia = f.id_familia
WHERE ma.eh_responsavel = TRUE
ORDER BY f.nome;

-- 2.4 - Contar membros por família
SELECT
    f.nome AS familia,
    COUNT(m.cpf) AS total_membros,
    COUNT(ma.cpf) AS adultos,
    COUNT(mc.cpf) AS criancas
FROM familia f
         LEFT JOIN membro m ON f.id_familia = m.id_familia
         LEFT JOIN membro_adulto ma ON m.cpf = ma.cpf
         LEFT JOIN membro_crianca mc ON m.cpf = mc.cpf
GROUP BY f.id_familia, f.nome
ORDER BY total_membros DESC;

-- ==============================
-- 3. CONSULTAS DE TELEFONES
-- ==============================

-- 3.1 - Listar telefones de uma família
SELECT
    f.nome AS familia,
    m.nome AS membro,
    t.numero
FROM familia f
         JOIN membro m ON f.id_familia = m.id_familia
         JOIN telefone t ON m.cpf = t.cpf_membro
WHERE f.id_familia = 1
ORDER BY m.nome;

-- 3.2 - Buscar membro por telefone
SELECT
    m.nome,
    m.cpf,
    f.nome AS familia,
    t.numero
FROM telefone t
         JOIN membro m ON t.cpf_membro = m.cpf
         JOIN familia f ON m.id_familia = f.id_familia
WHERE t.numero = '(61)99999-1111';

-- ==============================
-- 4. CONSULTAS DE PRÁTICAS E CATEGORIAS
-- ==============================

-- 4.1 - Listar todas as práticas por categoria
SELECT
    cp.nome AS categoria,
    cp.fator_multiplicador,
    ps.nome AS pratica,
    ps.descricao,
    ps.pontos_base,
    ps.dificuldade,
    ps.frequencia_esperada
FROM categoria_pratica cp
         JOIN pratica_sustentavel ps ON cp.id_categoria = ps.id_categoria
ORDER BY cp.nome, ps.dificuldade, ps.pontos_base DESC;

-- 4.2 - Buscar práticas por dificuldade
SELECT
    ps.nome AS pratica,
    cp.nome AS categoria,
    ps.pontos_base,
    ps.descricao
FROM pratica_sustentavel ps
         JOIN categoria_pratica cp ON ps.id_categoria = cp.id_categoria
WHERE ps.dificuldade = 'FACIL'
ORDER BY ps.pontos_base DESC;

-- 4.3 - Contar práticas por categoria
SELECT
    cp.nome AS categoria,
    COUNT(ps.id_pratica) AS total_praticas,
    AVG(ps.pontos_base) AS media_pontos
FROM categoria_pratica cp
         LEFT JOIN pratica_sustentavel ps ON cp.id_categoria = ps.id_categoria
GROUP BY cp.id_categoria, cp.nome
ORDER BY total_praticas DESC;

-- ==============================
-- 5. CONSULTAS DE METAS
-- ==============================

-- 5.1 - Listar todas as metas ativas
SELECT
    m.titulo,
    m.descricao,
    m.tipo,
    m.pontos_objetivo,
    m.data_inicio,
    m.data_fim,
    DATEDIFF(m.data_fim, CURDATE()) AS dias_restantes,
    f.nome AS criada_por
FROM meta m
         LEFT JOIN familia f ON m.criado_por_familia = f.id_familia
WHERE m.status = 'ATIVA'
ORDER BY m.data_fim;

-- 5.2 - Metas por tipo
SELECT
    tipo,
    COUNT(*) AS total_metas,
    COUNT(CASE WHEN status = 'ATIVA' THEN 1 END) AS ativas,
    COUNT(CASE WHEN status = 'CONCLUIDA' THEN 1 END) AS concluidas
FROM meta
GROUP BY tipo;

-- 5.3 - Metas próximas do vencimento (próximos 30 dias)
SELECT
    m.titulo,
    m.tipo,
    m.data_fim,
    DATEDIFF(m.data_fim, CURDATE()) AS dias_restantes
FROM meta m
WHERE m.status = 'ATIVA'
  AND DATEDIFF(m.data_fim, CURDATE()) BETWEEN 0 AND 30
ORDER BY dias_restantes;

-- ==============================
-- 6. CONSULTAS DE FAMÍLIA-META (M:N)
-- ==============================

-- 6.1 - Metas de uma família específica
SELECT
    m.titulo,
    m.descricao,
    m.tipo,
    m.pontos_objetivo,
    fm.progresso_individual,
    fm.data_associacao,
    fm.observacoes,
    DATEDIFF(m.data_fim, CURDATE()) AS dias_restantes
FROM meta m
         JOIN familia_meta fm ON m.id_meta = fm.id_meta
         JOIN familia f ON fm.id_familia = f.id_familia
WHERE f.nome = 'Família Silva' AND fm.ativa = TRUE
ORDER BY fm.progresso_individual DESC;

-- 6.2 - Famílias participando de uma meta
SELECT
    f.nome AS familia,
    fm.progresso_individual,
    fm.data_associacao,
    fm.observacoes
FROM familia f
         JOIN familia_meta fm ON f.id_familia = fm.id_familia
         JOIN meta m ON fm.id_meta = m.id_meta
WHERE m.titulo = 'Plantar árvores no bairro' AND fm.ativa = TRUE
ORDER BY fm.progresso_individual DESC;

-- 6.3 - Progresso de metas comunitárias
SELECT
    m.titulo,
    m.tipo,
    COUNT(fm.id_familia) AS familias_participando,
    AVG(fm.progresso_individual) AS progresso_medio,
    MAX(fm.progresso_individual) AS melhor_progresso,
    MIN(fm.progresso_individual) AS menor_progresso
FROM meta m
         LEFT JOIN familia_meta fm ON m.id_meta = fm.id_meta AND fm.ativa = TRUE
WHERE m.tipo = 'COMUNITARIA'
GROUP BY m.id_meta
ORDER BY progresso_medio DESC;

-- ==============================
-- 7. CONSULTAS DE REGISTROS DIÁRIOS
-- ==============================

-- 7.1 - Registros de hoje por família
SELECT
    f.nome AS familia,
    m.nome AS membro,
    ps.nome AS pratica,
    rd.quantidade_realizada,
    rd.pontos_ganhos
FROM familia f
         JOIN membro m ON f.id_familia = m.id_familia
         JOIN registro_diario rd ON m.cpf = rd.cpf_membro
         JOIN pratica_sustentavel ps ON rd.id_pratica = ps.id_pratica
WHERE rd.data_registro = CURDATE()
ORDER BY f.nome, rd.pontos_ganhos DESC;

-- 7.2 - Histórico de um membro (últimos 7 dias)
SELECT
    rd.data_registro,
    ps.nome AS pratica,
    cp.nome AS categoria,
    rd.quantidade_realizada,
    rd.pontos_ganhos
FROM registro_diario rd
         JOIN pratica_sustentavel ps ON rd.id_pratica = ps.id_pratica
         JOIN categoria_pratica cp ON ps.id_categoria = cp.id_categoria
WHERE rd.cpf_membro = '12345678901'
  AND rd.data_registro >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
ORDER BY rd.data_registro DESC;

-- 7.3 - Práticas mais realizadas (ranking)
SELECT
    ps.nome AS pratica,
    cp.nome AS categoria,
    COUNT(rd.id_registro) AS total_realizacoes,
    SUM(rd.pontos_ganhos) AS pontos_totais,
    AVG(rd.pontos_ganhos) AS media_pontos
FROM pratica_sustentavel ps
         JOIN categoria_pratica cp ON ps.id_categoria = cp.id_categoria
         JOIN registro_diario rd ON ps.id_pratica = rd.id_pratica
GROUP BY ps.id_pratica
ORDER BY total_realizacoes DESC
    LIMIT 10;

-- ==============================
-- 8. CONSULTAS DE CONQUISTAS
-- ==============================

-- 8.1 - Todas as conquistas alcançadas
SELECT
    m.titulo AS meta,
    c.id_conquista,
    c.data_conquista,
    c.pontos_bonus,
    f.nome AS familia_criadora
FROM conquista c
         JOIN meta m ON c.id_meta = m.id_meta
         LEFT JOIN familia f ON m.criado_por_familia = f.id_familia
ORDER BY c.data_conquista DESC;

-- 8.2 - Conquistas de uma meta específica
SELECT
    c.id_conquista,
    c.data_conquista,
    c.pontos_bonus
FROM conquista c
WHERE c.id_meta = 1
ORDER BY c.data_conquista;

-- ==============================
-- 9. CONSULTAS DE RELATÓRIOS
-- ==============================

-- 9.1 - Relatório mensal atual
SELECT
    f.nome AS familia,
    rm.total_pontos_familia,
    rm.total_praticas_realizadas,
    RANK() OVER (ORDER BY rm.total_pontos_familia DESC) AS ranking
FROM familia f
         JOIN relatorio_mensal rm ON f.id_familia = rm.id_familia
WHERE rm.mes_ano = '2025-02-01'
ORDER BY rm.total_pontos_familia DESC;

-- 9.2 - Evolução mensal de uma família
SELECT
    rm.mes_ano,
    rm.total_pontos_familia,
    rm.total_praticas_realizadas
FROM relatorio_mensal rm
WHERE rm.id_familia = 1
ORDER BY rm.mes_ano DESC;

-- ==============================
-- 10. RANKING E ESTATÍSTICAS
-- ==============================

-- 10.1 - Ranking de famílias (mês atual)
SELECT
    f.nome AS familia,
    COALESCE(SUM(rd.pontos_ganhos), 0) AS pontos_mes,
    COUNT(DISTINCT rd.id_registro) AS praticas_realizadas,
    RANK() OVER (ORDER BY COALESCE(SUM(rd.pontos_ganhos), 0) DESC) AS posicao
FROM familia f
         LEFT JOIN membro m ON f.id_familia = m.id_familia
         LEFT JOIN registro_diario rd ON m.cpf = rd.cpf_membro
    AND MONTH(rd.data_registro) = MONTH(CURDATE())
    AND YEAR(rd.data_registro) = YEAR(CURDATE())
GROUP BY f.id_familia, f.nome
ORDER BY pontos_mes DESC;

-- 10.2 - Estatísticas gerais do sistema
SELECT
    (SELECT COUNT(*) FROM familia) AS total_familias,
    (SELECT COUNT(*) FROM membro) AS total_membros,
    (SELECT COUNT(*) FROM meta WHERE status = 'ATIVA') AS metas_ativas,
    (SELECT COUNT(*) FROM pratica_sustentavel) AS praticas_disponiveis,
    (SELECT COUNT(*) FROM registro_diario WHERE data_registro = CURDATE()) AS registros_hoje;

-- 10.3 - Top 5 membros mais ativos
SELECT
    m.nome AS membro,
    f.nome AS familia,
    COUNT(rd.id_registro) AS total_registros,
    SUM(rd.pontos_ganhos) AS total_pontos
FROM membro m
         JOIN familia f ON m.id_familia = f.id_familia
         JOIN registro_diario rd ON m.cpf = rd.cpf_membro
WHERE rd.data_registro >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY m.cpf, m.nome, f.nome
ORDER BY total_pontos DESC
    LIMIT 5;