-- =====================================================
-- Sistema de Gestão de Resíduos e Reciclagem
-- Script de Testes das Automações
-- Oracle SQL - Demonstração das Triggers
-- =====================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

-- =====================================================
-- TESTE 1: Atualização de Capacidade ao Adicionar Descarte
-- =====================================================

PROMPT 
PROMPT ========================================================
PROMPT TESTE 1: Descarte Correto - Atualização de Capacidade
PROMPT ========================================================
PROMPT

-- Consulta capacidade antes
SELECT ID_CONTAINER, LOCALIZACAO, CAPACIDADE_ATUAL, PERCENTUAL_OCUPACAO, STATUS
FROM CONTAINER
WHERE ID_CONTAINER = 4;

PROMPT
PROMPT Adicionando descarte de 20 kg no container 4 (Metal)...
PROMPT

-- Adiciona descarte correto
INSERT INTO RESIDUO_DESCARTADO (
    ID_DESCARTE, ID_CONTAINER, ID_TIPO_RESIDUO, 
    PESO_KG, DATA_DESCARTE, OBSERVACAO
) VALUES (
    SEQ_RESIDUO_DESCARTADO.NEXTVAL, 4, 4, 
    20.0, SYSDATE, 'Teste de automação - latas de alumínio'
);

COMMIT;

PROMPT
PROMPT Consultando capacidade após o descarte:
PROMPT

-- Consulta capacidade depois
SELECT ID_CONTAINER, LOCALIZACAO, CAPACIDADE_ATUAL, PERCENTUAL_OCUPACAO, STATUS
FROM CONTAINER
WHERE ID_CONTAINER = 4;

-- =====================================================
-- TESTE 2: Geração de Alerta ao Atingir 80%
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT TESTE 2: Alerta de Capacidade ao Atingir 80%
PROMPT ========================================================
PROMPT

-- Adiciona descartes para atingir 80%
PROMPT Adicionando descartes para atingir 80% no container 4...
PROMPT

INSERT INTO RESIDUO_DESCARTADO (
    ID_DESCARTE, ID_CONTAINER, ID_TIPO_RESIDUO, 
    PESO_KG, DATA_DESCARTE, OBSERVACAO
) VALUES (
    SEQ_RESIDUO_DESCARTADO.NEXTVAL, 4, 4, 
    30.0, SYSDATE, 'Teste - mais metal'
);

COMMIT;

PROMPT
PROMPT Verificando notificações geradas:
PROMPT

SELECT ID_NOTIFICACAO, TIPO_NOTIFICACAO, MENSAGEM, PRIORIDADE, STATUS
FROM NOTIFICACAO
WHERE ID_CONTAINER = 4
ORDER BY DATA_GERACAO DESC
FETCH FIRST 3 ROWS ONLY;

-- =====================================================
-- TESTE 3: Validação de Descarte Incorreto
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT TESTE 3: Descarte Incorreto e Notificação
PROMPT ========================================================
PROMPT

PROMPT Tentando descartar Plástico (tipo 1) no container de Metal (container 4)...
PROMPT

-- Tenta descarte incorreto
INSERT INTO RESIDUO_DESCARTADO (
    ID_DESCARTE, ID_CONTAINER, ID_TIPO_RESIDUO, 
    PESO_KG, DATA_DESCARTE, OBSERVACAO
) VALUES (
    SEQ_RESIDUO_DESCARTADO.NEXTVAL, 4, 1, 
    5.0, SYSDATE, 'Teste de descarte incorreto'
);

COMMIT;

PROMPT
PROMPT Verificando se foi marcado como descarte incorreto:
PROMPT

SELECT ID_DESCARTE, DESCARTE_CORRETO, PESO_KG, OBSERVACAO
FROM RESIDUO_DESCARTADO
WHERE ID_CONTAINER = 4
ORDER BY DATA_DESCARTE DESC
FETCH FIRST 1 ROWS ONLY;

PROMPT
PROMPT Verificando notificação de descarte incorreto:
PROMPT

SELECT ID_NOTIFICACAO, TIPO_NOTIFICACAO, MENSAGEM, PRIORIDADE
FROM NOTIFICACAO
WHERE ID_CONTAINER = 4 AND TIPO_NOTIFICACAO = 'DESCARTE_INCORRETO'
ORDER BY DATA_GERACAO DESC
FETCH FIRST 1 ROWS ONLY;

-- =====================================================
-- TESTE 4: Atingir 100% - Container Cheio
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT TESTE 4: Container Atingindo 100% - Status CHEIO
PROMPT ========================================================
PROMPT

-- Adiciona descarte para completar 100%
PROMPT Adicionando descarte para completar 100% no container 4...
PROMPT

INSERT INTO RESIDUO_DESCARTADO (
    ID_DESCARTE, ID_CONTAINER, ID_TIPO_RESIDUO, 
    PESO_KG, DATA_DESCARTE, OBSERVACAO
) VALUES (
    SEQ_RESIDUO_DESCARTADO.NEXTVAL, 4, 4, 
    35.0, SYSDATE, 'Teste - preenchendo até 100%'
);

COMMIT;

PROMPT
PROMPT Status do container após atingir 100%:
PROMPT

SELECT ID_CONTAINER, CAPACIDADE_ATUAL, CAPACIDADE_MAXIMA, 
       PERCENTUAL_OCUPACAO, STATUS
FROM CONTAINER
WHERE ID_CONTAINER = 4;

PROMPT
PROMPT Notificações de container cheio:
PROMPT

SELECT TIPO_NOTIFICACAO, MENSAGEM, PRIORIDADE, STATUS
FROM NOTIFICACAO
WHERE ID_CONTAINER = 4 AND TIPO_NOTIFICACAO = 'CONTAINER_CHEIO'
ORDER BY DATA_GERACAO DESC
FETCH FIRST 1 ROWS ONLY;

-- =====================================================
-- TESTE 5: Reset de Container Após Coleta
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT TESTE 5: Reset de Container Após Coleta Realizada
PROMPT ========================================================
PROMPT

PROMPT Status do container antes da coleta:
PROMPT

SELECT ID_CONTAINER, CAPACIDADE_ATUAL, PERCENTUAL_OCUPACAO, STATUS
FROM CONTAINER
WHERE ID_CONTAINER = 4;

PROMPT
PROMPT Agendando e realizando coleta no container 4...
PROMPT

-- Agenda coleta
INSERT INTO COLETA (
    ID_COLETA, ID_CONTAINER, EMPRESA_RESPONSAVEL,
    STATUS_COLETA, DATA_AGENDAMENTO
) VALUES (
    SEQ_COLETA.NEXTVAL, 4, 'MetalRecicla - Teste',
    'AGENDADA', SYSDATE
);

COMMIT;

-- Marca como realizada
UPDATE COLETA
SET STATUS_COLETA = 'REALIZADA',
    DATA_COLETA = SYSDATE,
    PESO_COLETADO = 90.0,
    DESTINO_FINAL = 'Fundição de Alumínio - Teste'
WHERE ID_CONTAINER = 4
  AND STATUS_COLETA = 'AGENDADA'
  AND DATA_AGENDAMENTO = TRUNC(SYSDATE);

COMMIT;

PROMPT
PROMPT Status do container após coleta realizada:
PROMPT

SELECT ID_CONTAINER, CAPACIDADE_ATUAL, PERCENTUAL_OCUPACAO, STATUS, ULTIMA_COLETA
FROM CONTAINER
WHERE ID_CONTAINER = 4;

PROMPT
PROMPT Notificação de coleta realizada:
PROMPT

SELECT TIPO_NOTIFICACAO, MENSAGEM, STATUS
FROM NOTIFICACAO
WHERE ID_CONTAINER = 4 AND TIPO_NOTIFICACAO = 'COLETA_REALIZADA'
ORDER BY DATA_GERACAO DESC
FETCH FIRST 1 ROWS ONLY;

PROMPT
PROMPT Verificando se alertas anteriores foram resolvidos:
PROMPT

SELECT TIPO_NOTIFICACAO, STATUS, DATA_RESOLUCAO
FROM NOTIFICACAO
WHERE ID_CONTAINER = 4 
  AND TIPO_NOTIFICACAO IN ('CONTAINER_CHEIO', 'ALERTA_CAPACIDADE')
ORDER BY DATA_GERACAO DESC;

-- =====================================================
-- RELATÓRIOS CONSOLIDADOS
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT RELATÓRIO CONSOLIDADO DE CONTAINERS
PROMPT ========================================================
PROMPT

EXECUTE PRC_RELATORIO_CONTAINERS;

PROMPT
PROMPT ========================================================
PROMPT RELATÓRIO DE DESCARTES INCORRETOS
PROMPT ========================================================
PROMPT

EXECUTE PRC_RELATORIO_DESCARTES_INCORRETOS;

-- =====================================================
-- ESTATÍSTICAS FINAIS
-- =====================================================

PROMPT
PROMPT ========================================================
PROMPT ESTATÍSTICAS DO SISTEMA
PROMPT ========================================================
PROMPT

SELECT 'Total de Containers' AS METRICA, COUNT(*) AS VALOR FROM CONTAINER
UNION ALL
SELECT 'Containers Ativos', COUNT(*) FROM CONTAINER WHERE STATUS = 'ATIVO'
UNION ALL
SELECT 'Containers Cheios', COUNT(*) FROM CONTAINER WHERE STATUS = 'CHEIO'
UNION ALL
SELECT 'Total de Descartes', COUNT(*) FROM RESIDUO_DESCARTADO
UNION ALL
SELECT 'Descartes Corretos', COUNT(*) FROM RESIDUO_DESCARTADO WHERE DESCARTE_CORRETO = 'S'
UNION ALL
SELECT 'Descartes Incorretos', COUNT(*) FROM RESIDUO_DESCARTADO WHERE DESCARTE_CORRETO = 'N'
UNION ALL
SELECT 'Total de Coletas', COUNT(*) FROM COLETA
UNION ALL
SELECT 'Coletas Realizadas', COUNT(*) FROM COLETA WHERE STATUS_COLETA = 'REALIZADA'
UNION ALL
SELECT 'Total de Notificações', COUNT(*) FROM NOTIFICACAO
UNION ALL
SELECT 'Notificações Pendentes', COUNT(*) FROM NOTIFICACAO WHERE STATUS = 'PENDENTE';

PROMPT
PROMPT ========================================================
PROMPT PESO TOTAL COLETADO POR TIPO DE RESÍDUO
PROMPT ========================================================
PROMPT

SELECT t.NOME AS TIPO_RESIDUO, 
       COUNT(c.ID_COLETA) AS NUM_COLETAS,
       NVL(SUM(c.PESO_COLETADO), 0) AS PESO_TOTAL_KG
FROM TIPO_RESIDUO t
LEFT JOIN CONTAINER con ON t.ID_TIPO_RESIDUO = con.ID_TIPO_RESIDUO
LEFT JOIN COLETA c ON con.ID_CONTAINER = c.ID_CONTAINER 
                   AND c.STATUS_COLETA = 'REALIZADA'
GROUP BY t.NOME
ORDER BY PESO_TOTAL_KG DESC;

PROMPT
PROMPT ========================================================
PROMPT TESTES CONCLUÍDOS COM SUCESSO!
PROMPT Todas as 4 automações foram validadas:
PROMPT   1. Atualização automática de capacidade ✓
PROMPT   2. Geração de alertas (80% e 100%) ✓
PROMPT   3. Validação de descarte incorreto ✓
PROMPT   4. Reset após coleta realizada ✓
PROMPT ========================================================
PROMPT
