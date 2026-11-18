 
USE mascovetd;
 
SELECT tipo_atencion, estado, COUNT(*) AS cantidad
FROM CITA WHERE activo=1 AND fecha BETWEEN '2026-10-01' AND '2026-11-30'
GROUP BY tipo_atencion, estado ORDER BY tipo_atencion, estado;

 
SELECT COALESCE(v.especialidad,'SIN ESPECIALIDAD') AS especialidad,
       COUNT(*) AS citas
FROM CITA c LEFT JOIN VETERINARIO v ON v.id_usuario = c.id_veterinario
WHERE c.activo=1 AND c.fecha BETWEEN '2026-10-01' AND '2026-11-30'
GROUP BY v.especialidad ORDER BY citas DESC;

 
SELECT c.fecha, m.nombre AS mascota, c.tipo_atencion, a.id_atencion
FROM ATENCION_MEDICA a
JOIN CITA c ON c.id_cita=a.id_cita
JOIN MASCOTA m ON m.id_mascota=c.id_mascota
JOIN USUARIO u ON u.id_usuario=m.id_usuario
WHERE a.activo=1 AND u.nombre_usuario='ana.rojas' AND m.nombre='Nina'
ORDER BY c.fecha, a.id_atencion;
