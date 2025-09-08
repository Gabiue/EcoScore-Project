-- ==============================
-- TABELA: FAMILIA
-- ==============================
CREATE TABLE familia (
    id_familia INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    rua VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    cep VARCHAR(15) NOT NULL,
    data_criacao DATE NOT NULL
);

-- ==============================
-- TABELA: MEMBRO
-- ==============================
CREATE TABLE membro (
    cpf CHAR(11) PRIMARY KEY,
    id_familia INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    papel_familia VARCHAR(50) NOT NULL,
    cpf_supervisor CHAR(11), -- Auto-relacionamento para supervisão parental
    -- idade é derivada, não armazenamos
    FOREIGN KEY (id_familia) REFERENCES familia(id_familia),
    FOREIGN KEY (cpf_supervisor) REFERENCES membro(cpf)
);

-- ==============================
-- TABELA: TELEFONE (atributo multivalorado de MEMBRO)
-- ==============================
CREATE TABLE telefone (
    id_telefone INT AUTO_INCREMENT PRIMARY KEY,
    cpf_membro CHAR(11) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    FOREIGN KEY (cpf_membro) REFERENCES membro(cpf)
);

-- ==============================
-- TABELA: CATEGORIA_PRATICA
-- ==============================
CREATE TABLE categoria_pratica (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    fator_multiplicador DECIMAL(5,2) NOT NULL
);

-- ==============================
-- TABELA: PRATICA_SUSTENTAVEL
-- ==============================
CREATE TABLE pratica_sustentavel (
    id_pratica INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(512),
    pontos_base INT NOT NULL CHECK (pontos_base > 0),
    dificuldade ENUM('FACIL', 'MEDIA', 'DIFICIL') NOT NULL,
    frequencia_esperada VARCHAR(20),
    FOREIGN KEY (id_categoria) REFERENCES categoria_pratica(id_categoria)
);

-- ==============================
-- TABELA: META
-- ==============================
CREATE TABLE meta (
    id_meta INT AUTO_INCREMENT PRIMARY KEY,
    id_familia INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(512),
    pontos_objetivo INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    status ENUM('ATIVA', 'CONCLUIDA', 'CANCELADA') NOT NULL,
    FOREIGN KEY (id_familia) REFERENCES familia(id_familia),
    CHECK (data_fim > data_inicio)
);

-- ==============================
-- TABELA: CONQUISTA (entidade fraca de META)
-- ==============================
CREATE TABLE conquista (
    id_conquista INT NOT NULL,
    id_meta INT NOT NULL,
    data_conquista DATE NOT NULL,
    pontos_bonus INT DEFAULT 0,
    PRIMARY KEY (id_conquista, id_meta),
    FOREIGN KEY (id_meta) REFERENCES meta(id_meta)
);

-- ==============================
-- TABELA: RELATORIO_MENSAL
-- ==============================
CREATE TABLE relatorio_mensal (
    id_relatorio INT AUTO_INCREMENT PRIMARY KEY,
    id_familia INT NOT NULL,
    mes_ano DATE NOT NULL, -- formato MM-YY
    total_pontos_familia INT NOT NULL,
    total_praticas_realizadas INT NOT NULL,
    FOREIGN KEY (id_familia) REFERENCES familia(id_familia)
);

-- ==============================
-- TABELA: REGISTRO_DIARIO (entidade associativa MEMBRO <-> PRATICA)
-- ==============================
CREATE TABLE registro_diario (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    cpf_membro CHAR(11) NOT NULL,
    id_pratica INT NOT NULL,
    data_registro DATE NOT NULL,
    quantidade_realizada INT NOT NULL,
    pontos_ganhos INT NOT NULL,
    FOREIGN KEY (cpf_membro) REFERENCES membro(cpf),
    FOREIGN KEY (id_pratica) REFERENCES pratica_sustentavel(id_pratica)
);

-- ==============================
-- ESPECIALIZAÇÕES: MEMBRO_ADULTO e MEMBRO_CRIANCA
-- ==============================
CREATE TABLE membro_adulto (
    cpf CHAR(11) PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    eh_responsavel BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (cpf) REFERENCES membro(cpf)
);

CREATE TABLE membro_crianca (
    cpf CHAR(11) PRIMARY KEY,
    cpf_responsavel CHAR(11) NOT NULL,
    bonus_escolar INT DEFAULT 0,
    FOREIGN KEY (cpf) REFERENCES membro(cpf),
    FOREIGN KEY (cpf_responsavel) REFERENCES membro_adulto(cpf)
);

-- ==============================
-- ÍNDICES PARA PERFORMANCE
-- ==============================
CREATE INDEX idx_registro_data ON registro_diario(data_registro);
CREATE INDEX idx_membro_familia ON membro(id_familia);
CREATE INDEX idx_telefone_membro ON telefone(cpf_membro);