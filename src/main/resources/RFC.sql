-- RFC1: historial de servicios por usuario de servicio.

CREATE OR REPLACE VIEW VW_HISTORICO_SERVICIOS_POR_USUARIO AS
SELECT
  s.id                                 AS servicio_id,
  s.id_usuarioServicio                  AS id_usuarioServicio,
  s.distancia_km                        AS distancia_km,
  s.id_tarifa                           AS id_tarifa,
  s.tipo_servicio                       AS tipo_servicio,
  s.fecha                               AS fecha,
  s.hora_inicio                         AS hora_inicio,
  s.costo                               AS costo,
  s.estado                              AS estado,
  uc.id                                 AS conductor_id,
  uc.nombre                             AS conductor_nombre,
  uc.cedula                             AS conductor_cedula,
  v.id                                  AS vehiculo_id,
  v.placa                               AS vehiculo_placa,
  LISTAGG( p.id || '|' || NVL(p.nombre,'') || '|' || NVL(p.direccion,'') || '|' || NVL(TO_CHAR(p.latitud),'') || '|' || NVL(TO_CHAR(p.longitud),''),
           ';;') WITHIN GROUP (ORDER BY p.orden) AS puntos,
  LISTAGG( r.id || '|' || NVL(TO_CHAR(r.calificacion),'') || '|' || NVL(r.comentario,''),
           ';;') WITHIN GROUP (ORDER BY r.id) AS revisiones
FROM SERVICIO s
LEFT JOIN USUARIO_CONDUCTOR uc ON uc.id = s.id_usuarioConductor
LEFT JOIN VEHICULO v ON v.id = s.id_vehiculo
LEFT JOIN PUNTO p ON p.id_ervicio = s.id
LEFT JOIN REVISION r ON r.id_servicio = s.id
GROUP BY
  s.id, s.id_usuarioServicio, s.distancia_km, s.id_tarifa, s.tipo_servicio, s.fecha, s.hora_inicio, s.costo, s.estado,
  uc.id, uc.nombre, uc.cedula, v.id, v.placa;

-- RFC2: conductores con más servicios prestados.
CREATE OR REPLACE VIEW VW_CONDUCTORES_SERVICIOS_COUNT AS
SELECT
  uc.id                                  AS conductor_id,
  uc.nombre                              AS conductor_nombre,
  uc.cedula                              AS conductor_cedula,
  uc.correo                              AS conductor_correo,
  uc.celular                             AS conductor_celular,
  COUNT(s.id)                            AS servicios_count,
  MAX(s.fecha)                           AS ultima_fecha_servicio
FROM USUARIO_CONDUCTOR uc
LEFT JOIN SERVICIO s ON s.id_usuarioConductor = uc.id
GROUP BY uc.id, uc.nombre, uc.cedula, uc.correo, uc.celular;

-- RFC3: Total de dinero obtenido por un conductor para cada uno de sus vehículos, discriminado por tipo de servicio.

CREATE OR REPLACE VIEW VW_GANANCIAS_CONDUCTOR_VEHICULO_POR_SERVICIO AS
SELECT
  uc.id                                AS conductor_id,
  uc.nombre                            AS conductor_nombre,
  v.id                                 AS vehiculo_id,
  v.placa                              AS vehiculo_placa,
  s.tipo_servicio                      AS tipo_servicio,
  COUNT(s.id)                          AS servicios_count,
  SUM(NVL(s.costo,0))                  AS ingreso_bruto,
  ROUND(SUM(NVL(s.costo,0)) * 0.20, 2) AS comision_alpescab,        -- comisión ALPESCAB (20%)
  ROUND(SUM(NVL(s.costo,0)) * 0.80, 2) AS ingreso_neto,             -- ingreso después de comisiones
  ROUND(SUM(NVL(s.costo,0)) OVER (PARTITION BY v.id), 2) AS total_bruto_por_vehiculo,
  ROUND(SUM(NVL(s.costo,0)) OVER (PARTITION BY v.id) * 0.20, 2) AS total_comision_por_vehiculo,
  ROUND(SUM(NVL(s.costo,0)) OVER (PARTITION BY v.id) * 0.80, 2) AS total_neto_por_vehiculo
FROM SERVICIO s
JOIN USUARIO_CONDUCTOR uc ON uc.id = s.id_usuarioConductor
JOIN VEHICULO v ON v.id = s.id_vehiculo
WHERE s.estado = 'FINALIZADO'
GROUP BY uc.id, uc.nombre, v.id, v.placa, s.tipo_servicio
ORDER BY uc.id, v.id, s.tipo_servicio;

-- RFC4: asocia cada servicio con la ciudad del punto de partida (orden = 1) y el nivel de tarifa.
CREATE OR REPLACE VIEW VW_USO_SERVICIOS_BASE AS
SELECT
  s.id                        AS servicio_id,
  s.fecha                     AS fecha,
  s.tipo_servicio             AS tipo_servicio,
  t.nivel                     AS tarifa_nivel,
  p.id_ciudad                 AS ciudad_id,
  c.nombre                    AS ciudad_nombre
FROM SERVICIO s
LEFT JOIN TARIFA t ON t.id = s.id_tarifa
LEFT JOIN PUNTO p ON p.id_ervicio = s.id AND p.orden = 1 -- asumimos punto orden=1 como punto de partida
LEFT JOIN CIUDAD c ON c.id = p.id_ciudad;