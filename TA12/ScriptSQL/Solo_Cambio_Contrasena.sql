 
 
USE mascovetd;
DELIMITER $$
 
 
 
DROP PROCEDURE IF EXISTS CAMBIAR_CONTRASENA_CUENTA_USUARIO$$
CREATE PROCEDURE CAMBIAR_CONTRASENA_CUENTA_USUARIO(
    OUT _resultado INT,
    IN _fid_usuario INT,
    IN _password_actual VARCHAR(255),
    IN _password_nueva VARCHAR(255))
BEGIN
    IF _password_nueva IS NULL OR CHAR_LENGTH(_password_nueva) < 8 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='La nueva contraseña debe tener al menos 8 caracteres';
    END IF;
    SET _resultado = 0;
    UPDATE CUENTA_USUARIO cu
    JOIN USUARIO u ON u.id_usuario = cu.fid_usuario
    SET cu.password = MD5(_password_nueva)
    WHERE cu.fid_usuario = _fid_usuario
      AND cu.activo = 1 AND u.activo = 1
      AND cu.password = MD5(_password_actual)
      AND cu.password <> MD5(_password_nueva)
      AND (EXISTS (SELECT 1 FROM CLIENTE c WHERE c.id_usuario = u.id_usuario AND c.activo = 1)
        OR EXISTS (SELECT 1 FROM VETERINARIO v WHERE v.id_usuario = u.id_usuario AND v.activo = 1)
        OR EXISTS (SELECT 1 FROM RECEPCIONISTA r WHERE r.id_usuario = u.id_usuario AND r.activo = 1))
      AND NOT EXISTS (SELECT 1 FROM ADMINISTRADOR a WHERE a.id_usuario = u.id_usuario AND a.activo = 1);
    SET _resultado = ROW_COUNT();
END$$

DELIMITER ;
