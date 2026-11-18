 
USE mascovetd;
CREATE TABLE IF NOT EXISTS CUENTA_USUARIO (
    id_cuenta_usuario INT NOT NULL AUTO_INCREMENT,
    fid_usuario INT NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_cuenta_usuario PRIMARY KEY (id_cuenta_usuario),
    CONSTRAINT fk_cuenta_usuario_usuario FOREIGN KEY (fid_usuario)
        REFERENCES USUARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);
START TRANSACTION;
INSERT INTO CUENTA_USUARIO(fid_usuario,username,password,activo)
SELECT u.id_usuario,u.nombre_usuario,MD5(u.contrasena),u.activo FROM USUARIO u
WHERE u.contrasena IS NOT NULL AND u.contrasena <> ''
AND NOT EXISTS(SELECT 1 FROM CUENTA_USUARIO c WHERE c.fid_usuario=u.id_usuario);
UPDATE USUARIO SET contrasena='' WHERE contrasena<>'';
COMMIT;
DELIMITER $$
DROP PROCEDURE IF EXISTS INSERTAR_CUENTA_USUARIO$$
CREATE PROCEDURE INSERTAR_CUENTA_USUARIO(
    OUT _id_cuenta_usuario INT, IN _fid_usuario INT, IN _username VARCHAR(100),
    IN _password VARCHAR(255), IN _activo BOOLEAN)
BEGIN
    IF _password IS NULL OR CHAR_LENGTH(_password) < 8 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='La contraseña debe tener al menos ocho caracteres';
    END IF;
    INSERT INTO CUENTA_USUARIO(fid_usuario,username,password,activo)
    VALUES (_fid_usuario,_username,MD5(_password),_activo);
    SET _id_cuenta_usuario=LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CUENTA_USUARIO$$
CREATE PROCEDURE MODIFICAR_CUENTA_USUARIO(
    OUT _resultado INT, IN _id_cuenta_usuario INT,
    IN _username VARCHAR(100), IN _password VARCHAR(255), IN _activo BOOLEAN)
BEGIN
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
IF _password IS NOT NULL AND _password <> '' AND CHAR_LENGTH(_password)<8 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='La nueva contraseña debe tener al menos ocho caracteres';
    END IF;
    UPDATE USUARIO u JOIN CUENTA_USUARIO cu ON cu.fid_usuario=u.id_usuario
      SET u.nombre_usuario=_username WHERE cu.id_cuenta_usuario=_id_cuenta_usuario;
    UPDATE CUENTA_USUARIO SET username=_username,
        password=CASE WHEN _password IS NOT NULL AND _password <> ''
             THEN MD5(_password) ELSE password END,
        activo=_activo
    WHERE id_cuenta_usuario=_id_cuenta_usuario;
    SET _resultado=ROW_COUNT();
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CUENTA_USUARIO$$
CREATE PROCEDURE ELIMINAR_CUENTA_USUARIO(OUT _resultado INT,IN _id_cuenta_usuario INT)
BEGIN
    UPDATE CUENTA_USUARIO SET activo=0 WHERE id_cuenta_usuario=_id_cuenta_usuario AND activo=1;
    SET _resultado=ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS VERIFICAR_CUENTA_USUARIO$$
CREATE PROCEDURE VERIFICAR_CUENTA_USUARIO(IN _username VARCHAR(100),IN _password VARCHAR(255))
BEGIN
    SELECT cu.id_cuenta_usuario, cu.fid_usuario, cu.username, cu.activo,
       CONCAT_WS(' ', u.nombre, u.apellido_paterno) AS nombre,
       u.nombre_usuario AS usuario_vinculado,
       CASE WHEN a.id_usuario IS NOT NULL THEN 'Administrador'
            WHEN v.id_usuario IS NOT NULL THEN 'Veterinario'
            WHEN r.id_usuario IS NOT NULL THEN 'Recepcionista'
            WHEN c.id_usuario IS NOT NULL THEN 'Cliente'
            ELSE NULL END AS rol
    FROM CUENTA_USUARIO cu
 JOIN USUARIO u ON u.id_usuario=cu.fid_usuario
 LEFT JOIN ADMINISTRADOR a ON a.id_usuario=u.id_usuario AND a.activo=1
 LEFT JOIN VETERINARIO v ON v.id_usuario=u.id_usuario AND v.activo=1
 LEFT JOIN RECEPCIONISTA r ON r.id_usuario=u.id_usuario AND r.activo=1
 LEFT JOIN CLIENTE c ON c.id_usuario=u.id_usuario AND c.activo=1
    WHERE cu.activo=1 AND u.activo=1
      AND (cu.username=_username OR (c.id_usuario IS NOT NULL AND c.correo=_username))
      AND cu.password=MD5(_password)
      AND (a.id_usuario IS NOT NULL OR v.id_usuario IS NOT NULL
           OR r.id_usuario IS NOT NULL OR c.id_usuario IS NOT NULL)
    LIMIT 1;
END$$

 
 
 
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

DROP PROCEDURE IF EXISTS LISTAR_CUENTA_USUARIO_X_ID$$
CREATE PROCEDURE LISTAR_CUENTA_USUARIO_X_ID(IN _id_cuenta_usuario INT)
BEGIN
    SELECT cu.id_cuenta_usuario, cu.fid_usuario, cu.username, cu.activo,
       CONCAT_WS(' ', u.nombre, u.apellido_paterno) AS nombre,
       u.nombre_usuario AS usuario_vinculado,
       CASE WHEN a.id_usuario IS NOT NULL THEN 'Administrador'
            WHEN v.id_usuario IS NOT NULL THEN 'Veterinario'
            WHEN r.id_usuario IS NOT NULL THEN 'Recepcionista'
            WHEN c.id_usuario IS NOT NULL THEN 'Cliente'
            ELSE NULL END AS rol
    FROM CUENTA_USUARIO cu
 JOIN USUARIO u ON u.id_usuario=cu.fid_usuario
 LEFT JOIN ADMINISTRADOR a ON a.id_usuario=u.id_usuario AND a.activo=1
 LEFT JOIN VETERINARIO v ON v.id_usuario=u.id_usuario AND v.activo=1
 LEFT JOIN RECEPCIONISTA r ON r.id_usuario=u.id_usuario AND r.activo=1
 LEFT JOIN CLIENTE c ON c.id_usuario=u.id_usuario AND c.activo=1
    WHERE cu.id_cuenta_usuario=_id_cuenta_usuario;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CUENTA_USUARIO_TODOS$$
CREATE PROCEDURE LISTAR_CUENTA_USUARIO_TODOS()
BEGIN
    SELECT cu.id_cuenta_usuario, cu.fid_usuario, cu.username, cu.activo,
       CONCAT_WS(' ', u.nombre, u.apellido_paterno) AS nombre,
       u.nombre_usuario AS usuario_vinculado,
       CASE WHEN a.id_usuario IS NOT NULL THEN 'Administrador'
            WHEN v.id_usuario IS NOT NULL THEN 'Veterinario'
            WHEN r.id_usuario IS NOT NULL THEN 'Recepcionista'
            WHEN c.id_usuario IS NOT NULL THEN 'Cliente'
            ELSE NULL END AS rol
    FROM CUENTA_USUARIO cu
 JOIN USUARIO u ON u.id_usuario=cu.fid_usuario
 LEFT JOIN ADMINISTRADOR a ON a.id_usuario=u.id_usuario AND a.activo=1
 LEFT JOIN VETERINARIO v ON v.id_usuario=u.id_usuario AND v.activo=1
 LEFT JOIN RECEPCIONISTA r ON r.id_usuario=u.id_usuario AND r.activo=1
 LEFT JOIN CLIENTE c ON c.id_usuario=u.id_usuario AND c.activo=1
    ORDER BY cu.id_cuenta_usuario;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_USUARIO_BASE$$
CREATE PROCEDURE INSERTAR_USUARIO_BASE(
    OUT _id_usuario INT,
    IN _dni VARCHAR(9), IN _nombre VARCHAR(50),
    IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50),
    IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN)
BEGIN
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
INSERT INTO USUARIO(dni, nombre, apellido_paterno, apellido_materno,
                        nombre_usuario, contrasena, activo)
    VALUES(_dni, _nombre, _apellido_paterno, _apellido_materno,
           _nombre_usuario, '', _activo);
    SET _id_usuario = LAST_INSERT_ID();
    CALL INSERTAR_CUENTA_USUARIO(@cuenta_nueva, _id_usuario, _nombre_usuario, _contrasena, _activo);
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_USUARIO_BASE$$
CREATE PROCEDURE MODIFICAR_USUARIO_BASE(
    OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50),
    IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50),
    IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255),
    IN _activo BOOLEAN, IN _id_usuario INT)
BEGIN
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
UPDATE USUARIO
    SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno,
        apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario,
        contrasena='', activo=_activo
    WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET username=_nombre_usuario,
        password=CASE WHEN _contrasena IS NOT NULL AND _contrasena<>'' THEN MD5(_contrasena) ELSE password END,
        activo=CASE WHEN _activo=0 THEN 0 ELSE activo END WHERE fid_usuario=_id_usuario;
    SET _resultado=GREATEST(_resultado,ROW_COUNT());
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_ADMINISTRADOR$$
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _cargo VARCHAR(100))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, '', _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO ADMINISTRADOR (id_usuario, cargo) VALUES (_id_usuario, _cargo);
    CALL INSERTAR_CUENTA_USUARIO(@cuenta_nueva, _id_usuario, _nombre_usuario, _contrasena, _activo);
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_ADMINISTRADOR$$
CREATE PROCEDURE MODIFICAR_ADMINISTRADOR(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _cargo VARCHAR(100))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena='', activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE ADMINISTRADOR SET cargo=_cargo WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET username=_nombre_usuario,
        password=CASE WHEN _contrasena IS NOT NULL AND _contrasena<>'' THEN MD5(_contrasena) ELSE password END,
        activo=CASE WHEN _activo=0 THEN 0 ELSE activo END WHERE fid_usuario=_id_usuario;
    SET _resultado=GREATEST(_resultado,ROW_COUNT());
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_ADMINISTRADOR$$
CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(OUT _resultado INT, IN _id_usuario INT)
BEGIN
UPDATE ADMINISTRADOR SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE USUARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
    UPDATE CUENTA_USUARIO SET activo=0 WHERE fid_usuario=_id_usuario;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CLIENTE$$
CREATE PROCEDURE INSERTAR_CLIENTE(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, '', _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO CLIENTE (id_usuario, correo, telefono) VALUES (_id_usuario, _correo, _telefono);
    CALL INSERTAR_CUENTA_USUARIO(@cuenta_nueva, _id_usuario, _nombre_usuario, _contrasena, _activo);
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CLIENTE$$
CREATE PROCEDURE MODIFICAR_CLIENTE(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena='', activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE CLIENTE SET correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET username=_nombre_usuario,
        password=CASE WHEN _contrasena IS NOT NULL AND _contrasena<>'' THEN MD5(_contrasena) ELSE password END,
        activo=CASE WHEN _activo=0 THEN 0 ELSE activo END WHERE fid_usuario=_id_usuario;
    SET _resultado=GREATEST(_resultado,ROW_COUNT());
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CLIENTE$$
CREATE PROCEDURE ELIMINAR_CLIENTE(OUT _resultado INT, IN _id_usuario INT)
BEGIN
UPDATE CLIENTE SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE USUARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
    UPDATE CUENTA_USUARIO SET activo=0 WHERE fid_usuario=_id_usuario;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_RECEPCIONISTA$$
CREATE PROCEDURE INSERTAR_RECEPCIONISTA(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _turno VARCHAR(150), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, '', _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO RECEPCIONISTA (id_usuario, turno, correo, telefono) VALUES (_id_usuario, _turno, _correo, _telefono);
    CALL INSERTAR_CUENTA_USUARIO(@cuenta_nueva, _id_usuario, _nombre_usuario, _contrasena, _activo);
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_RECEPCIONISTA$$
CREATE PROCEDURE MODIFICAR_RECEPCIONISTA(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _turno VARCHAR(150), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena='', activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE RECEPCIONISTA SET turno=_turno, correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET username=_nombre_usuario,
        password=CASE WHEN _contrasena IS NOT NULL AND _contrasena<>'' THEN MD5(_contrasena) ELSE password END,
        activo=CASE WHEN _activo=0 THEN 0 ELSE activo END WHERE fid_usuario=_id_usuario;
    SET _resultado=GREATEST(_resultado,ROW_COUNT());
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_RECEPCIONISTA$$
CREATE PROCEDURE ELIMINAR_RECEPCIONISTA(OUT _resultado INT, IN _id_usuario INT)
BEGIN
UPDATE RECEPCIONISTA SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE USUARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
    UPDATE CUENTA_USUARIO SET activo=0 WHERE fid_usuario=_id_usuario;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_VETERINARIO$$
CREATE PROCEDURE INSERTAR_VETERINARIO(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _num_colegiatura VARCHAR(10), IN _especialidad VARCHAR(20), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, '', _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO VETERINARIO (id_usuario, num_colegiatura, especialidad, correo, telefono) VALUES (_id_usuario, _num_colegiatura, _especialidad, _correo, _telefono);
    CALL INSERTAR_CUENTA_USUARIO(@cuenta_nueva, _id_usuario, _nombre_usuario, _contrasena, _activo);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_VETERINARIO$$
CREATE PROCEDURE MODIFICAR_VETERINARIO(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _num_colegiatura VARCHAR(10), IN _especialidad VARCHAR(20), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena='', activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE VETERINARIO SET num_colegiatura=_num_colegiatura, especialidad=_especialidad, correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET username=_nombre_usuario,
        password=CASE WHEN _contrasena IS NOT NULL AND _contrasena<>'' THEN MD5(_contrasena) ELSE password END,
        activo=CASE WHEN _activo=0 THEN 0 ELSE activo END WHERE fid_usuario=_id_usuario;
    SET _resultado=GREATEST(_resultado,ROW_COUNT());
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_VETERINARIO$$
CREATE PROCEDURE ELIMINAR_VETERINARIO(OUT _resultado INT, IN _id_usuario INT)
BEGIN
UPDATE VETERINARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE USUARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
    UPDATE CUENTA_USUARIO SET activo=0 WHERE fid_usuario=_id_usuario;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_USUARIO$$
CREATE PROCEDURE ELIMINAR_USUARIO(OUT _resultado INT, IN _id_usuario INT)
BEGIN
UPDATE CLIENTE SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    UPDATE VETERINARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    UPDATE RECEPCIONISTA SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    UPDATE ADMINISTRADOR SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    UPDATE USUARIO SET activo=0 WHERE id_usuario=_id_usuario AND activo=1;
    SET _resultado = ROW_COUNT();
    UPDATE CUENTA_USUARIO SET activo=0 WHERE fid_usuario=_id_usuario;
END$$
DELIMITER ;
