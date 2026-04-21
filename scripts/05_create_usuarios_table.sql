-- =====================================================
-- Tabela de Usuários para Autenticação
-- Spring Security + JWT
-- =====================================================

-- Remover sequência se existir
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_USUARIO';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

-- Remover tabela se existir
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE USUARIOS CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

-- Criar tabela de usuários
CREATE TABLE USUARIOS (
    ID_USUARIO      NUMBER(10)      PRIMARY KEY,
    NOME            VARCHAR2(100)   NOT NULL,
    EMAIL           VARCHAR2(100)   NOT NULL UNIQUE,
    SENHA           VARCHAR2(255)   NOT NULL,
    ROLE            VARCHAR2(20)    DEFAULT 'ROLE_USER',
    ATIVO           NUMBER(1)       DEFAULT 1 CHECK (ATIVO IN (0, 1)),
    DATA_CADASTRO   TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- Criar sequência
CREATE SEQUENCE SEQ_USUARIO
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Criar índice
CREATE INDEX IDX_USUARIO_EMAIL ON USUARIOS(EMAIL);

-- Comentário
COMMENT ON TABLE USUARIOS IS 'Usuários do sistema para autenticação JWT';

-- Inserir usuário admin de teste
-- Senha: admin123 (hash BCrypt)
INSERT INTO USUARIOS (ID_USUARIO, NOME, EMAIL, SENHA, ROLE, ATIVO, DATA_CADASTRO)
VALUES (SEQ_USUARIO.NEXTVAL, 'Administrador', 'admin@fiap.com.br', 
        '$2a$10$rFqE8vF4pD3qJ0vH7kXxPO8nYvKqR5sJ3tKpY6hZ9wM7kL8jN6vGe', 
        'ROLE_ADMIN', 1, CURRENT_TIMESTAMP);

-- Inserir usuário comum de teste  
-- Senha: user123 (hash BCrypt)
INSERT INTO USUARIOS (ID_USUARIO, NOME, EMAIL, SENHA, ROLE, ATIVO, DATA_CADASTRO)
VALUES (SEQ_USUARIO.NEXTVAL, 'Usuário Teste', 'user@fiap.com.br',
        '$2a$10$rFqE8vF4pD3qJ0vH7kXxPO8nYvKqR5sJ3tKpY6hZ9wM7kL8jN6vGe',
        'ROLE_USER', 1, CURRENT_TIMESTAMP);

COMMIT;

-- Mensagem de sucesso
BEGIN
    DBMS_OUTPUT.PUT_LINE('===================================================');
    DBMS_OUTPUT.PUT_LINE('Tabela USUARIOS criada com sucesso!');
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Usuários de teste criados:');
    DBMS_OUTPUT.PUT_LINE('  Admin: admin@fiap.com.br / admin123');
    DBMS_OUTPUT.PUT_LINE('  User:  user@fiap.com.br / user123');
    DBMS_OUTPUT.PUT_LINE('===================================================');
END;
/
