-- Datos de prueba dependientes de los usuarios y sus roles.
-- Ejecutar después de ScriptUsuariosPrueba.sql.

USE mascovetd;
START TRANSACTION;

INSERT INTO MASCOTA (id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo)
SELECT u.id_usuario, s.mascota, s.nacimiento, s.especie, s.raza, s.sexo, 1
FROM (
    SELECT 'prueba_cliente_ana' AS usuario, 'Nina' AS mascota, '2022-04-12' AS nacimiento,
        'PERRO' AS especie, 'Mestiza' AS raza, 'HEMBRA' AS sexo
    UNION ALL SELECT 'prueba_cliente_ana', 'Milo', '2021-06-10', 'GATO', 'Siames', 'MACHO'
    UNION ALL SELECT 'prueba_cliente_bruno', 'Kira', '2023-02-08', 'PERRO', 'Golden Retriever', 'HEMBRA'
    UNION ALL SELECT 'prueba_cliente_bruno', 'Coco', '2024-08-02', 'CONEJO', 'Mini Lop', 'MACHO'
    UNION ALL SELECT 'prueba_cliente_carla', 'Luna', '2020-12-17', 'PERRO', 'Schnauzer', 'HEMBRA'
) s
JOIN USUARIO u ON u.nombre_usuario=s.usuario
JOIN CLIENTE c ON c.id_usuario=u.id_usuario
WHERE NOT EXISTS (SELECT 1 FROM MASCOTA m WHERE m.id_usuario=u.id_usuario AND m.nombre=s.mascota);

INSERT INTO HORARIO_ATENCION (id_veterinario, dia, hora_inicio, hora_fin, activo)
SELECT u.id_usuario, d.dia, '08:00:00', '13:00:00', 1
FROM USUARIO u
CROSS JOIN (
    SELECT 'LUNES' AS dia UNION ALL SELECT 'MARTES' UNION ALL SELECT 'MIERCOLES'
    UNION ALL SELECT 'JUEVES' UNION ALL SELECT 'VIERNES'
) d
JOIN VETERINARIO v ON v.id_usuario=u.id_usuario
WHERE u.nombre_usuario IN ('prueba_veterinario_luis','prueba_veterinario_elena','prueba_veterinario_mario')
AND NOT EXISTS (SELECT 1 FROM HORARIO_ATENCION h WHERE h.id_veterinario=u.id_usuario
               AND h.dia=d.dia AND h.hora_inicio='08:00:00' AND h.hora_fin='13:00:00');

INSERT INTO HORARIO_ATENCION (id_veterinario, dia, hora_inicio, hora_fin, activo)
SELECT u.id_usuario, 'SABADO', '09:00:00', '12:00:00', 1
FROM USUARIO u JOIN VETERINARIO v ON v.id_usuario=u.id_usuario
WHERE u.nombre_usuario='prueba_veterinario_luis'
AND NOT EXISTS (SELECT 1 FROM HORARIO_ATENCION h WHERE h.id_veterinario=u.id_usuario
               AND h.dia='SABADO' AND h.hora_inicio='09:00:00' AND h.hora_fin='12:00:00');

INSERT INTO CITA (id_mascota, tipo_atencion, fecha, hora, estado, motivo_cancelacion,
                  id_recepcionista, id_veterinario, activo)
SELECT m.id_mascota, s.tipo_atencion, s.fecha, s.hora, s.estado, s.motivo_cancelacion,
       r.id_usuario, v.id_usuario, 1
FROM (
    SELECT 'prueba_cliente_ana' AS cliente, 'Nina' AS mascota, 'CONSULTA' AS tipo_atencion,
           '2026-10-20' AS fecha, '09:00:00' AS hora, 'ATENDIDA' AS estado,
           NULL AS motivo_cancelacion, 'prueba_recepcionista_diana' AS recepcionista,
           'prueba_veterinario_luis' AS veterinario
    UNION ALL SELECT 'prueba_cliente_ana','Nina','CONTROL','2026-11-03','10:00:00','ATENDIDA',NULL,'prueba_recepcionista_diana','prueba_veterinario_luis'
    UNION ALL SELECT 'prueba_cliente_ana','Nina','VACUNACION','2026-10-22','11:00:00','ATENDIDA',NULL,'prueba_recepcionista_diana','prueba_veterinario_luis'
    UNION ALL SELECT 'prueba_cliente_ana','Milo','CIRUGIA','2026-10-19','10:00:00','ATENDIDA',NULL,'prueba_recepcionista_jorge','prueba_veterinario_elena'
    UNION ALL SELECT 'prueba_cliente_bruno','Kira','VACUNACION','2026-11-06','11:30:00','ATENDIDA',NULL,'prueba_recepcionista_jorge','prueba_veterinario_luis'
    UNION ALL SELECT 'prueba_cliente_bruno','Coco','CONSULTA','2026-11-05','09:00:00','ATENDIDA',NULL,'prueba_recepcionista_diana','prueba_veterinario_mario'
    UNION ALL SELECT 'prueba_cliente_bruno','Kira','CONSULTA','2026-11-16','09:00:00','RESERVADA',NULL,'prueba_recepcionista_diana','prueba_veterinario_luis'
    UNION ALL SELECT 'prueba_cliente_ana','Milo','CONTROL','2026-11-16','10:00:00','CONFIRMADA',NULL,'prueba_recepcionista_jorge','prueba_veterinario_elena'
    UNION ALL SELECT 'prueba_cliente_bruno','Coco','VACUNACION','2026-11-17','11:00:00','RESERVADA',NULL,'prueba_recepcionista_diana','prueba_veterinario_luis'
    UNION ALL SELECT 'prueba_cliente_ana','Nina','CONSULTA','2026-11-18','10:00:00','CANCELADA','Viaje del propietario','prueba_recepcionista_jorge','prueba_veterinario_mario'
    UNION ALL SELECT 'prueba_cliente_carla','Luna','CONSULTA','2026-11-19','12:00:00','RESERVADA',NULL,'prueba_recepcionista_diana','prueba_veterinario_mario'
    UNION ALL SELECT 'prueba_cliente_bruno','Coco','VACUNACION','2026-11-20','09:00:00','RESERVADA',NULL,'prueba_recepcionista_diana','prueba_veterinario_luis'
) s
JOIN USUARIO uc ON uc.nombre_usuario=s.cliente
JOIN MASCOTA m ON m.id_usuario=uc.id_usuario AND m.nombre=s.mascota
JOIN USUARIO r ON r.nombre_usuario=s.recepcionista
JOIN RECEPCIONISTA rp ON rp.id_usuario=r.id_usuario
JOIN USUARIO v ON v.nombre_usuario=s.veterinario
JOIN VETERINARIO vt ON vt.id_usuario=v.id_usuario
WHERE NOT EXISTS (SELECT 1 FROM CITA c WHERE c.id_mascota=m.id_mascota
                  AND c.fecha=s.fecha AND c.hora=s.hora);

INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_cita, peso_actual, alergias, activo)
SELECT s.observaciones, s.hora_inicio, s.hora_fin, c.id_cita, s.peso, s.alergias, 1
FROM (
    SELECT 'prueba_cliente_ana' AS cliente, 'Nina' AS mascota, '2026-10-20' AS fecha,
           'Paciente estable' AS observaciones, '09:05:00' AS hora_inicio, '09:35:00' AS hora_fin,
           12.4 AS peso, 'Ninguna' AS alergias
    UNION ALL SELECT 'prueba_cliente_ana','Nina','2026-11-03','Buena evolucion','10:05:00','10:25:00',12.2,'Ninguna'
    UNION ALL SELECT 'prueba_cliente_ana','Nina','2026-10-22','Vacunacion preventiva','11:05:00','11:20:00',12.4,'Ninguna'
    UNION ALL SELECT 'prueba_cliente_ana','Milo','2026-10-19','Procedimiento sin complicaciones','10:05:00','11:40:00',4.3,'Ninguna'
    UNION ALL SELECT 'prueba_cliente_bruno','Kira','2026-11-06','Vacunacion anual','11:35:00','11:50:00',19.0,'Ninguna'
    UNION ALL SELECT 'prueba_cliente_bruno','Coco','2026-11-05','Consulta dermatologica','09:05:00','09:30:00',2.1,'Ninguna'
) s
JOIN USUARIO u ON u.nombre_usuario=s.cliente
JOIN MASCOTA m ON m.id_usuario=u.id_usuario AND m.nombre=s.mascota
JOIN CITA c ON c.id_mascota=m.id_mascota AND c.fecha=s.fecha AND c.estado='ATENDIDA'
WHERE NOT EXISTS (SELECT 1 FROM ATENCION_MEDICA a WHERE a.id_cita=c.id_cita);

INSERT INTO CONSULTA (id_atencion, motivoConsulta, diagnostico, tratamiento, activo)
SELECT a.id_atencion,
    CASE WHEN m.nombre='Nina' THEN 'Picazon de orejas' ELSE 'Enrojecimiento de piel' END,
    CASE WHEN m.nombre='Nina' THEN 'Otitis externa leve' ELSE 'Dermatitis leve' END,
    CASE WHEN m.nombre='Nina' THEN 'Limpieza y gotas' ELSE 'Control ambiental y crema' END, 1
FROM ATENCION_MEDICA a JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE u.nombre_usuario IN ('prueba_cliente_ana','prueba_cliente_bruno')
AND c.tipo_atencion='CONSULTA' AND c.fecha IN ('2026-10-20','2026-11-05')
AND NOT EXISTS (SELECT 1 FROM CONSULTA con WHERE con.id_atencion=a.id_atencion);

INSERT INTO CONTROL (id_atencion, evolucion, indicaciones, activo)
SELECT a.id_atencion, 'Disminucion de molestias', 'Continuar observacion 7 dias', 1
FROM ATENCION_MEDICA a JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE u.nombre_usuario='prueba_cliente_ana' AND m.nombre='Nina' AND c.fecha='2026-11-03'
AND NOT EXISTS (SELECT 1 FROM CONTROL co WHERE co.id_atencion=a.id_atencion);

INSERT INTO CIRUGIA (id_atencion, procedimiento, indicaciones_post_operatorias, activo)
SELECT a.id_atencion, 'Esterilizacion programada', 'Reposo y control de herida por 10 dias', 1
FROM ATENCION_MEDICA a JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE u.nombre_usuario='prueba_cliente_ana' AND m.nombre='Milo' AND c.fecha='2026-10-19'
AND NOT EXISTS (SELECT 1 FROM CIRUGIA ci WHERE ci.id_atencion=a.id_atencion);

INSERT INTO VACUNACION (id_atencion, fechaAplicacion, fechaProximaDosis, dosis, activo)
SELECT a.id_atencion, c.fecha,
       CASE WHEN m.nombre='Nina' THEN '2027-10-22' ELSE '2027-11-06' END,
       '1 dosis', 1
FROM ATENCION_MEDICA a JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE u.nombre_usuario IN ('prueba_cliente_ana','prueba_cliente_bruno')
AND c.tipo_atencion='VACUNACION' AND c.fecha IN ('2026-10-22','2026-11-06')
AND NOT EXISTS (SELECT 1 FROM VACUNACION va WHERE va.id_atencion=a.id_atencion);

INSERT INTO VACUNA (nombre, descripcion, activo)
SELECT s.nombre, s.descripcion, 1
FROM (
  SELECT 'Rabia' AS nombre, 'Prevencion de rabia canina y felina' AS descripcion
  UNION ALL SELECT 'Polivalente canina', 'Proteccion contra varias enfermedades caninas'
  UNION ALL SELECT 'Triple felina', 'Proteccion combinada para gatos'
  UNION ALL SELECT 'Parvovirus', 'Prevencion de parvovirosis canina'
) s
WHERE NOT EXISTS (SELECT 1 FROM VACUNA v WHERE v.nombre=s.nombre);

INSERT INTO VACUNACION_VACUNA (id_vacunacion, id_vacuna, activo)
SELECT va.id_atencion, v.id_vacuna, 1
FROM VACUNACION va JOIN ATENCION_MEDICA a ON a.id_atencion=va.id_atencion
JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
JOIN VACUNA v ON v.nombre=CASE WHEN m.nombre='Nina' THEN 'Rabia' ELSE 'Polivalente canina' END
WHERE (u.nombre_usuario='prueba_cliente_ana' AND m.nombre='Nina' AND c.fecha='2026-10-22')
   OR (u.nombre_usuario='prueba_cliente_bruno' AND m.nombre='Kira' AND c.fecha='2026-11-06')
ON DUPLICATE KEY UPDATE id_vacunacion=id_vacunacion;

INSERT INTO MEDICAMENTO (nombre, descripcion, fecha_vencimiento, monto, activo)
SELECT s.nombre, s.descripcion, s.fecha_vencimiento, s.monto, 1
FROM (
    SELECT 'Gotas oticas' AS nombre, 'Solucion para limpieza de oidos' AS descripcion,
           '2028-12-31' AS fecha_vencimiento, 28.50 AS monto
    UNION ALL SELECT 'Antiseptico', 'Limpieza de heridas', '2028-09-30', 20.00
    UNION ALL SELECT 'Antiinflamatorio', 'Uso veterinario bajo receta', '2028-06-30', 35.00
    UNION ALL SELECT 'Crema dermatologica', 'Cuidado de piel animal', '2028-08-31', 32.00
) s
WHERE NOT EXISTS (SELECT 1 FROM MEDICAMENTO me WHERE me.nombre=s.nombre);

INSERT INTO RECETA (id_atencion, fecha, indicaciones, activo)
SELECT a.id_atencion, c.fecha,
       CASE WHEN m.nombre='Nina' THEN 'Aplicar gotas segun indicacion por 5 dias'
            ELSE 'Aplicar crema segun indicacion por 7 dias' END, 1
FROM ATENCION_MEDICA a JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE c.tipo_atencion='CONSULTA'
AND ((u.nombre_usuario='prueba_cliente_ana' AND m.nombre='Nina' AND c.fecha='2026-10-20')
  OR (u.nombre_usuario='prueba_cliente_bruno' AND m.nombre='Coco' AND c.fecha='2026-11-05'))
AND NOT EXISTS (SELECT 1 FROM RECETA r WHERE r.id_atencion=a.id_atencion);

INSERT INTO DETALLE_RECETA (id_receta, id_medicamento, dosis, frecuencia, duracion, montoTotal, activo)
SELECT r.id_receta, med.id_medicamento,
       CASE WHEN m.nombre='Nina' THEN '2 gotas' ELSE 'Capa fina' END,
       CASE WHEN m.nombre='Nina' THEN 'Cada 12 horas' ELSE 'Cada 24 horas' END,
       CASE WHEN m.nombre='Nina' THEN '5 dias' ELSE '7 dias' END,
       med.monto, 1
FROM RECETA r JOIN ATENCION_MEDICA a ON a.id_atencion=r.id_atencion
JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
JOIN MEDICAMENTO med ON med.nombre=CASE WHEN m.nombre='Nina' THEN 'Gotas oticas' ELSE 'Crema dermatologica' END
WHERE ((u.nombre_usuario='prueba_cliente_ana' AND m.nombre='Nina' AND c.fecha='2026-10-20')
   OR (u.nombre_usuario='prueba_cliente_bruno' AND m.nombre='Coco' AND c.fecha='2026-11-05'))
AND NOT EXISTS (SELECT 1 FROM DETALLE_RECETA d WHERE d.id_receta=r.id_receta AND d.id_medicamento=med.id_medicamento);

COMMIT;

SELECT 'Clientes' AS modulo, COUNT(*) AS cantidad FROM CLIENTE c
    JOIN USUARIO u ON u.id_usuario=c.id_usuario WHERE u.nombre_usuario LIKE 'prueba_cliente_%'
UNION ALL SELECT 'Veterinarios', COUNT(*) FROM VETERINARIO v
    JOIN USUARIO u ON u.id_usuario=v.id_usuario WHERE u.nombre_usuario LIKE 'prueba_veterinario_%'
UNION ALL SELECT 'Mascotas', COUNT(*) FROM MASCOTA m
    JOIN USUARIO u ON u.id_usuario=m.id_usuario WHERE u.nombre_usuario LIKE 'prueba_cliente_%'
UNION ALL SELECT 'Horarios', COUNT(*) FROM HORARIO_ATENCION h
    JOIN USUARIO u ON u.id_usuario=h.id_veterinario WHERE u.nombre_usuario LIKE 'prueba_veterinario_%'
UNION ALL SELECT 'Citas', COUNT(*) FROM CITA c JOIN MASCOTA m ON m.id_mascota=c.id_mascota
    JOIN USUARIO u ON u.id_usuario=m.id_usuario WHERE u.nombre_usuario LIKE 'prueba_cliente_%'
UNION ALL SELECT 'Atenciones medicas', COUNT(*) FROM ATENCION_MEDICA a
    JOIN CITA c ON c.id_cita=a.id_cita JOIN MASCOTA m ON m.id_mascota=c.id_mascota
    JOIN USUARIO u ON u.id_usuario=m.id_usuario WHERE u.nombre_usuario LIKE 'prueba_cliente_%';
