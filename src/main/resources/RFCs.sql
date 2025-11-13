--RFC1

SELECT 
    s.id AS id_servicio,
    s.tipoServicio,
    s.fecha,
    s.costo,
    s.distanciaKm,
    c.nombre AS ciudad,
    uc.nombre AS nombre_conductor,
    uc.cedula AS cedula_conductor,
    uc.correo AS correo_conductor,
    v.placa,
    v.marca,
    v.modelo,
    v.nivel,
    p.direccion AS punto,
    p.orden
FROM SERVICIO s
JOIN USUARIO_CONDUCTOR uc ON s.id_usuarioConductor = uc.id
JOIN VEHICULO v ON s.id_vehiculo = v.id
JOIN PUNTO p ON s.id = p.id_servicio
JOIN CIUDAD c ON p.id_ciudad = c.id
WHERE s.id_usuarioServicio = :idUsuarioServicio
ORDER BY s.fecha DESC, p.orden ASC;


--RFC2
SELECT 
    uc.id AS id_conductor,
    uc.nombre,
    uc.cedula,
    uc.correo,
    COUNT(s.id) AS total_servicios
FROM USUARIO_CONDUCTOR uc
JOIN SERVICIO s ON uc.id = s.id_usuarioConductor
GROUP BY uc.id, uc.nombre, uc.cedula, uc.correo
ORDER BY total_servicios DESC
FETCH FIRST 20 ROWS ONLY;


--RFC3

SELECT 
    uc.id AS id_conductor,
    uc.nombre AS nombre_conductor,
    v.id AS id_vehiculo,
    v.placa,
    v.marca,
    v.modelo,
    COUNT(s.id) AS total_servicios,
    SUM(s.costo) AS total_bruto,
    SUM(s.costo * 0.9) AS total_neto
FROM SERVICIO s
JOIN VEHICULO v ON s.id_vehiculo = v.id
JOIN USUARIO_CONDUCTOR uc ON v.id_usuarioConductor = uc.id
GROUP BY uc.id, uc.nombre, v.id, v.placa, v.marca, v.modelo
ORDER BY uc.id, v.id;

--RFC4

SELECT 
    c.id AS id_ciudad,
    c.nombre AS ciudad,
    s.tipoServicio,
    COUNT(s.id) AS cantidad_servicios,
    ROUND(
        COUNT(s.id) * 100 /
        (
            SELECT COUNT(*)
            FROM SERVICIO s2
            JOIN PUNTO p2 ON s2.id = p2.id_servicio
            WHERE p2.id_ciudad = c.id
              AND s2.fecha BETWEEN TO_DATE(:fechaInicio, 'YYYY-MM-DD') 
                               AND TO_DATE(:fechaFin, 'YYYY-MM-DD')
        )
    , 2) AS porcentaje
FROM SERVICIO s
JOIN PUNTO p ON s.id = p.id_servicio
JOIN CIUDAD c ON p.id_ciudad = c.id
WHERE s.fecha BETWEEN TO_DATE(:fechaInicio, 'YYYY-MM-DD') 
                  AND TO_DATE(:fechaFin, 'YYYY-MM-DD')
GROUP BY c.id, c.nombre, s.tipoServicio
ORDER BY cantidad_servicios DESC;
