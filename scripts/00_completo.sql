-- =====================================================
-- Sistema de Gestão de Resíduos e Reciclagem
-- Script Completo - Todas as Etapas
-- Oracle SQL + PL/SQL
-- =====================================================
-- 
-- Este arquivo consolida todos os scripts em ordem:
-- 1. Criação das tabelas e sequences
-- 2. Inserção de dados de teste
-- 3. Criação das automações (TRIGGERs)
-- 4. Procedures auxiliares
--
-- Execute este arquivo completo no DataGrip ou SQL Developer
-- para criar todo o sistema de uma vez.
--
-- =====================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

PROMPT =====================================================
PROMPT INICIANDO CRIAÇÃO DO SISTEMA
PROMPT Sistema de Gestão de Resíduos e Reciclagem
PROMPT =====================================================

-- =====================================================
-- ETAPA 1: LIMPEZA E CRIAÇÃO DAS TABELAS
-- =====================================================

PROMPT
PROMPT [1/4] Criando estrutura do banco de dados...
PROMPT

@@01_create_tables.sql

-- =====================================================
-- ETAPA 2: INSERÇÃO DE DADOS DE TESTE
-- =====================================================

PROMPT
PROMPT [2/4] Inserindo dados de teste...
PROMPT

@@02_insert_data.sql

-- =====================================================
-- ETAPA 3: CRIAÇÃO DAS AUTOMAÇÕES
-- =====================================================

PROMPT
PROMPT [3/4] Criando automações (TRIGGERs e PROCEDUREs)...
PROMPT

@@03_automations.sql

-- =====================================================
-- ETAPA 4: VERIFICAÇÃO FINAL
-- =====================================================

PROMPT
PROMPT [4/4] Verificando instalação...
PROMPT

-- Verifica tabelas criadas
SELECT 'Tabelas criadas: ' || COUNT(*) FROM USER_TABLES 
WHERE TABLE_NAME IN ('TIPO_RESIDUO', 'CONTAINER', 'RESIDUO_DESCARTADO', 'COLETA', 'NOTIFICACAO');

-- Verifica sequences criadas
SELECT 'Sequences criadas: ' || COUNT(*) FROM USER_SEQUENCES 
WHERE SEQUENCE_NAME LIKE 'SEQ_%';

-- Verifica triggers criadas
SELECT 'Triggers criadas: ' || COUNT(*) FROM USER_TRIGGERS 
WHERE TRIGGER_NAME LIKE 'TRG_%';

-- Verifica procedures criadas
SELECT 'Procedures criadas: ' || COUNT(*) FROM USER_PROCEDURES 
WHERE OBJECT_NAME LIKE 'PRC_%';

PROMPT
PROMPT =====================================================
PROMPT INSTALAÇÃO CONCLUÍDA COM SUCESSO!
PROMPT =====================================================
PROMPT
PROMPT Sistema completo instalado:
PROMPT   - 5 Tabelas criadas
PROMPT   - 5 Sequences criadas  
PROMPT   - 4 Triggers de automação
PROMPT   - 2 Procedures auxiliares
PROMPT   - Dados de teste inseridos
PROMPT
PROMPT Próximos passos:
PROMPT   1. Visualize o diagrama ER no DataGrip
PROMPT   2. Execute: @@04_test_automations.sql para testar
PROMPT   3. Execute: EXEC PRC_RELATORIO_CONTAINERS; para relatório
PROMPT
PROMPT =====================================================
