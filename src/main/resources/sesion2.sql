-- -- Sesion 2: RF8

-- -- Ver conductores
-- SELECT uc.id, v.id as vehiculo_id
-- FROM USUARIO_CONDUCTOR uc
-- JOIN VEHICULO v ON uc.id = v.id_usuarioConductor
-- WHERE uc.estado = 'DISPONIBLE' AND v.nivel = 'ESTANDAR'
-- AND ROWNUM = 1;

-- -- Marcar ocupado
-- UPDATE USUARIO_CONDUCTOR SET estado = 'OCUPADO' WHERE id = 27;

-- -- Insertar servicio
-- INSERT INTO SERVICIO (distanciaKm, costo, tipoServicio, fecha, idTarifa, id_vehiculo, id_usuarioConductor, id_usuarioServicio)
-- VALUES (10.5, 9419.62, 'ESTANDAR', SYSDATE, 1, 27, 27, 1);

-- -- Ver ID del servicio
-- SELECT MAX(id) FROM SERVICIO;

-- -- Puntos
-- INSERT INTO PUNTO (direccion, longitud, latitud, orden, id_servicio, id_ciudad)
-- VALUES ('Carrera 7', -74.05, 4.68, 1, 1025, 1);

-- INSERT INTO PUNTO (direccion, longitud, latitud, orden, id_servicio, id_ciudad)
-- VALUES ('Calle 26', -74.09, 4.64, 2, 1025, 1);

-- COMMIT;

-- SELECT COUNT(*) FROM SERVICIO WHERE id_usuarioServicio = 1;