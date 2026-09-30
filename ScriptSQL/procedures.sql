USE prog3;

DELIMITER //
-- ASIGNAR USUARIOS A LOS TIPOS DE USUARIOS

DROP PROCEDURE IF EXISTS asignar_cliente //

CREATE PROCEDURE asignar_cliente(
    IN p_dni VARCHAR(9),
    IN p_correo VARCHAR(200),
    IN p_telefono VARCHAR(10)
)
BEGIN
    DECLARE v_id_usuario INT;

    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    INSERT INTO CLIENTE (
        id_usuario,
        correo,
        telefono
    )
    VALUES (
        v_id_usuario,
        p_correo,
        p_telefono
    );
END //


DROP PROCEDURE IF EXISTS asignar_veterinario //

CREATE PROCEDURE asignar_veterinario(
    IN p_dni VARCHAR(9),
    IN p_num_colegiatura VARCHAR(200),
    IN p_especialidad VARCHAR(255)
)
BEGIN
    DECLARE v_id_usuario INT;

    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    INSERT INTO VETERINARIO (
        id_usuario,
        num_colegiatura,
        especialidad
    )
    VALUES (
        v_id_usuario,
        p_num_colegiatura,
        p_especialidad
    );
END //


DROP PROCEDURE IF EXISTS asignar_recepcionista //

CREATE PROCEDURE asignar_recepcionista(
    IN p_dni VARCHAR(9),
    IN p_turno VARCHAR(150)
)
BEGIN
    DECLARE v_id_usuario INT;

    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    INSERT INTO RECEPCIONISTA (
        id_usuario,
        turno
    )
    VALUES (
        v_id_usuario,
        p_turno
    );
END //


DROP PROCEDURE IF EXISTS asignar_administrador //

CREATE PROCEDURE asignar_administrador(
    IN p_dni VARCHAR(9),
    IN p_cargo VARCHAR(100)
)
BEGIN
    DECLARE v_id_usuario INT;

    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    INSERT INTO ADMINISTRADOR (
        id_usuario,
        cargo
    )
    VALUES (
        v_id_usuario,
        p_cargo
    );
END //

DELIMITER ;

SELECT * FROM CLIENTE;
SELECT * FROM VETERINARIO;
SELECT * FROM RECEPCIONISTA;
SELECT * FROM ADMINISTRADOR;


-- ASIGNAR HORARIOS AL VETERINARIO

DELIMITER //

DROP PROCEDURE IF EXISTS asignar_horario_veterinario //

CREATE PROCEDURE asignar_horario_veterinario(
    IN p_dni VARCHAR(9),
    IN p_dia VARCHAR(100),
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME
)
BEGIN
    DECLARE v_id_usuario INT;
    DECLARE v_es_veterinario INT DEFAULT 0;

    -- Buscar al usuario segun DNI
    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    -- Verificar que sea veterinario
    SELECT COUNT(*)
    INTO v_es_veterinario
    FROM VETERINARIO
    WHERE id_usuario = v_id_usuario;

    IF v_es_veterinario = 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario indicado por DNI no es un veterinario';

    ELSE

        -- Asignar horario
        INSERT INTO HORARIO_ATENCION (
            id_veterinario,
            dia,
            hora_inicio,
            hora_fin
        )
        VALUES (
            v_id_usuario,
            p_dia,
            p_hora_inicio,
            p_hora_fin
        );

    END IF;

END //

DELIMITER ;

-- VINCULAR UNA MASCOTA A UN USUARIO (un usuario puede tener más de una mascota)

DELIMITER //

DROP PROCEDURE IF EXISTS vincular_mascota //

CREATE PROCEDURE vincular_mascota(
    IN p_dni VARCHAR(9),
    IN p_nombre VARCHAR(150),
    IN p_fechaNacimiento DATE,
    IN p_especie VARCHAR(150),
    IN p_raza VARCHAR(150),
    IN p_sexo VARCHAR(10)
)
BEGIN
    DECLARE v_id_usuario INT;
    DECLARE v_es_cliente INT DEFAULT 0;

    -- Buscar al cliente segun DNI
    SELECT id_usuario
    INTO v_id_usuario
    FROM USUARIO
    WHERE dni = p_dni;

    -- Verificar que el usuario sea cliente
    SELECT COUNT(*)
    INTO v_es_cliente
    FROM CLIENTE
    WHERE id_usuario = v_id_usuario;

    IF v_es_cliente = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario indicado por DNI no es un cliente';
    ELSE
        -- Asignar mascota
        INSERT INTO MASCOTA(
            id_usuario,
            nombre,
            fechaNacimiento,
            especie,
            raza,
            sexo
        )
        VALUES(
            v_id_usuario,
            p_nombre,
            p_fechaNacimiento,
            p_especie,
            p_raza,
            p_sexo
        );

    END IF;
END //

DELIMITER ;

-- REGISTRAR UNA CITA

DELIMITER //

DROP PROCEDURE IF EXISTS registrar_cita //

CREATE PROCEDURE registrar_cita(
    IN p_dni_cliente VARCHAR(9),
    IN p_nombre_mascota VARCHAR(150),
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_estado VARCHAR(150),
    IN p_dni_veterinario VARCHAR(9)
)
BEGIN
    DECLARE v_id_cliente INT;
    DECLARE v_id_mascota INT;
    DECLARE v_id_veterinario INT;
    DECLARE v_es_cliente INT DEFAULT 0;
    DECLARE v_es_veterinario INT DEFAULT 0;

    -- Buscar al cliente por DNI
    SELECT id_usuario
    INTO v_id_cliente
    FROM USUARIO
    WHERE dni = p_dni_cliente;

    -- Verificar que sea cliente
    SELECT COUNT(*)
    INTO v_es_cliente
    FROM CLIENTE
    WHERE id_usuario = v_id_cliente;

    IF v_es_cliente = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario indicado no es un cliente';
    END IF;

    -- Buscar la mascota del cliente
    SELECT id_mascota
    INTO v_id_mascota
    FROM MASCOTA
    WHERE id_usuario = v_id_cliente
      AND nombre = p_nombre_mascota;

    -- Verificar que la mascota exista
    IF v_id_mascota IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La mascota no pertenece al cliente o no existe';
    END IF;

    -- Buscar al veterinario por DNI
    SELECT id_usuario
    INTO v_id_veterinario
    FROM USUARIO
    WHERE dni = p_dni_veterinario;

    -- Verificar que sea veterinario
    SELECT COUNT(*)
    INTO v_es_veterinario
    FROM VETERINARIO
    WHERE id_usuario = v_id_veterinario;

    IF v_es_veterinario = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario indicado no es un veterinario';
    END IF;

    -- Registrar la cita
    INSERT INTO CITA(
        id_mascota,
        fecha,
        hora,
        estado,
        id_usuario,
        id_veterinario
    )
    VALUES(
        v_id_mascota,
        p_fecha,
        p_hora,
        p_estado,
        v_id_cliente,
        v_id_veterinario
    );

END //

DELIMITER ;

-- REGISTRAR UNA ATENCION MEDICA

DELIMITER //

DROP PROCEDURE IF EXISTS crear_atencion_medica //

CREATE PROCEDURE crear_atencion_medica(
    IN p_dni_cliente VARCHAR(9),
    IN p_nombre_mascota VARCHAR(150),
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_observaciones VARCHAR(200),
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME
)
BEGIN
    DECLARE v_id_cliente INT;
    DECLARE v_id_mascota INT;
    DECLARE v_id_cita INT;
    DECLARE v_es_cliente INT DEFAULT 0;

    -- Buscar al cliente por DNI
    SELECT id_usuario
    INTO v_id_cliente
    FROM USUARIO
    WHERE dni = p_dni_cliente;

    -- Verificar que sea cliente
    SELECT COUNT(*)
    INTO v_es_cliente
    FROM CLIENTE
    WHERE id_usuario = v_id_cliente;

    IF v_es_cliente = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario indicado no es un cliente';
    END IF;

    -- Buscar la mascota del cliente
    SELECT id_mascota
    INTO v_id_mascota
    FROM MASCOTA
    WHERE id_usuario = v_id_cliente
      AND nombre = p_nombre_mascota;

    -- Verificar que exista
    IF v_id_mascota IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La mascota no pertenece al cliente o no existe';
    END IF;

    -- Buscar la cita de la mascota
    SELECT id_cita
    INTO v_id_cita
    FROM CITA
    WHERE id_mascota = v_id_mascota
      AND fecha = p_fecha
      AND hora = p_hora;

    -- Verificar que exista la cita
    IF v_id_cita IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No existe una cita para esa mascota en la fecha y hora indicadas';
    END IF;

    -- Crear la atención médica
    INSERT INTO ATENCION_MEDICA(
        observaciones,
        hora_inicio,
        hora_fin,
        id_mascota,
        id_cita
    )
    VALUES(
        p_observaciones,
        p_hora_inicio,
        p_hora_fin,
        v_id_mascota,
        v_id_cita
    );

END //

DELIMITER ;



