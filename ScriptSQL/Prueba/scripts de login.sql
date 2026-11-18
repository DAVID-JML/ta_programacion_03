DELIMITER $$

DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_PARA_LOGIN$$
CREATE PROCEDURE BUSCAR_CLIENTE_PARA_LOGIN(IN p_correo VARCHAR(200))
BEGIN
    SELECT u.id_usuario,
           CONCAT(u.nombre, ' ', u.apellido_paterno) AS nombre,
           u.nombre_usuario,
           u.contrasena AS hash_contrasena
    FROM usuario u
    INNER JOIN cliente c ON c.id_usuario = u.id_usuario   -- solo clientes
    WHERE u.correo = p_correo                             -- sin distinguir mayúsculas (collation _ci)
      AND u.activo = 1
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_PERSONAL_PARA_LOGIN$$
CREATE PROCEDURE BUSCAR_PERSONAL_PARA_LOGIN(IN p_nombre_usuario VARCHAR(100))
BEGIN
    SELECT u.id_usuario,
           CONCAT(u.nombre, ' ', u.apellido_paterno) AS nombre,
           u.nombre_usuario,
           u.contrasena AS hash_contrasena,
           CASE
               WHEN a.id_usuario IS NOT NULL THEN 'ADMINISTRADOR'
               WHEN v.id_usuario IS NOT NULL THEN 'VETERINARIO'
               WHEN r.id_usuario IS NOT NULL THEN 'RECEPCIONISTA'
           END AS rol
    FROM usuario u
    LEFT JOIN administrador a ON a.id_usuario = u.id_usuario
    LEFT JOIN veterinario  v ON v.id_usuario = u.id_usuario
    LEFT JOIN recepcionista r ON r.id_usuario = u.id_usuario
    WHERE u.nombre_usuario = p_nombre_usuario
      AND u.activo = 1                                    -- RF007: cuenta deshabilitada no entra
      AND (a.id_usuario IS NOT NULL
           OR v.id_usuario IS NOT NULL
           OR r.id_usuario IS NOT NULL)                   -- debe ser personal, no cliente
    LIMIT 1;
END$$

DELIMITER ;