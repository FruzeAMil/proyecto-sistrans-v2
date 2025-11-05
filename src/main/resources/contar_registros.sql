-- ====================================
-- SCRIPT PARA CONTAR REGISTROS EN CADA TABLA
-- ====================================
-- Este script muestra cuántos registros hay en cada tabla de la base de datos

SET SERVEROUTPUT ON;

DECLARE
    v_count_ciudad NUMBER;
    v_count_conductor NUMBER;
    v_count_pasajero NUMBER;
    v_count_vehiculo NUMBER;
    v_count_servicio NUMBER;
    v_count_viaje NUMBER;
    v_count_punto NUMBER;
    v_count_revision NUMBER;
BEGIN
    -- Contar registros en cada tabla
    SELECT COUNT(*) INTO v_count_ciudad FROM CIUDAD;
    SELECT COUNT(*) INTO v_count_conductor FROM USUARIO_CONDUCTOR;
    SELECT COUNT(*) INTO v_count_pasajero FROM USUARIO_SERVICIO;
    SELECT COUNT(*) INTO v_count_vehiculo FROM VEHICULO;
    SELECT COUNT(*) INTO v_count_servicio FROM SERVICIO;
    SELECT COUNT(*) INTO v_count_viaje FROM VIAJE;
    SELECT COUNT(*) INTO v_count_punto FROM PUNTO;
    SELECT COUNT(*) INTO v_count_revision FROM REVISION;
    
    -- Mostrar resultados
    DBMS_OUTPUT.PUT_LINE('====================================');
    DBMS_OUTPUT.PUT_LINE('CONTEO DE REGISTROS POR TABLA');
    DBMS_OUTPUT.PUT_LINE('====================================');
    DBMS_OUTPUT.PUT_LINE('CIUDAD:             ' || v_count_ciudad || ' registros');
    DBMS_OUTPUT.PUT_LINE('USUARIO_CONDUCTOR:  ' || v_count_conductor || ' registros');
    DBMS_OUTPUT.PUT_LINE('USUARIO_SERVICIO:   ' || v_count_pasajero || ' registros');
    DBMS_OUTPUT.PUT_LINE('VEHICULO:           ' || v_count_vehiculo || ' registros');
    DBMS_OUTPUT.PUT_LINE('SERVICIO:           ' || v_count_servicio || ' registros');
    DBMS_OUTPUT.PUT_LINE('VIAJE:              ' || v_count_viaje || ' registros');
    DBMS_OUTPUT.PUT_LINE('PUNTO:              ' || v_count_punto || ' registros');
    DBMS_OUTPUT.PUT_LINE('REVISION:           ' || v_count_revision || ' registros');
    DBMS_OUTPUT.PUT_LINE('====================================');
    DBMS_OUTPUT.PUT_LINE('TOTAL GENERAL:      ' || 
        (v_count_ciudad + v_count_conductor + v_count_pasajero + v_count_vehiculo + 
         v_count_servicio + v_count_viaje + v_count_punto + v_count_revision) || ' registros');
    DBMS_OUTPUT.PUT_LINE('====================================');
    
    -- Verificación de integridad
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('VERIFICACION DE INTEGRIDAD:');
    DBMS_OUTPUT.PUT_LINE('====================================');
    
    IF v_count_servicio = v_count_viaje THEN
        DBMS_OUTPUT.PUT_LINE('✓ OK: Cada SERVICIO tiene un VIAJE');
    ELSE
        DBMS_OUTPUT.PUT_LINE('✗ ERROR: SERVICIO (' || v_count_servicio || ') != VIAJE (' || v_count_viaje || ')');
    END IF;
    
    IF v_count_punto = (v_count_servicio * 2) THEN
        DBMS_OUTPUT.PUT_LINE('✓ OK: Cada SERVICIO tiene 2 PUNTOS (origen y destino)');
    ELSE
        DBMS_OUTPUT.PUT_LINE('✗ ERROR: PUNTO (' || v_count_punto || ') != SERVICIO*2 (' || (v_count_servicio * 2) || ')');
    END IF;
    
    IF v_count_conductor = v_count_vehiculo THEN
        DBMS_OUTPUT.PUT_LINE('✓ OK: Cada CONDUCTOR tiene un VEHICULO');
    ELSE
        DBMS_OUTPUT.PUT_LINE('✗ ADVERTENCIA: CONDUCTOR (' || v_count_conductor || ') != VEHICULO (' || v_count_vehiculo || ')');
    END IF;
    
    DBMS_OUTPUT.PUT_LINE('====================================');
END;
/
