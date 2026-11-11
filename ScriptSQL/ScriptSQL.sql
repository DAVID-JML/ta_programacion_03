DROP DATABASE IF EXISTS mascovetd;
CREATE DATABASE mascovetd
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE mascovetd;

CREATE TABLE USUARIO (
    id_usuario      INT NOT NULL AUTO_INCREMENT,
    dni             VARCHAR(9) NOT NULL,
    nombre            VARCHAR(50) NOT NULL,
    apellido_paterno  VARCHAR(50) NOT NULL,
    apellido_materno  VARCHAR(50) NOT NULL,
    nombre_usuario  VARCHAR(100) NOT NULL,
    contrasena      VARCHAR(255) NOT NULL,
    activo          TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_dni UNIQUE (dni),
    CONSTRAINT uq_usuario_nombre_usuario UNIQUE (nombre_usuario)
);

CREATE TABLE CLIENTE (
    id_usuario  INT NOT NULL,
    correo      VARCHAR(200) NOT NULL,
    telefono    VARCHAR(20) NOT NULL,
    activo      TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_cliente PRIMARY KEY (id_usuario),
    CONSTRAINT uq_cliente_correo UNIQUE (correo),
    CONSTRAINT uq_cliente_telefono UNIQUE (telefono),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE VETERINARIO (
    id_usuario       INT NOT NULL,
    num_colegiatura  VARCHAR(10) NOT NULL,
    especialidad     VARCHAR(20) NOT NULL,
    correo           VARCHAR(200) NOT NULL,
    telefono         VARCHAR(20) NOT NULL,
    activo           TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_veterinario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_veterinario_num_colegiatura UNIQUE (num_colegiatura),
    CONSTRAINT fk_veterinario_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE RECEPCIONISTA (
    id_usuario  INT NOT NULL,
    turno       VARCHAR(150) NOT NULL,
    correo      VARCHAR(200) NOT NULL,
    telefono    VARCHAR(20) NOT NULL,
    activo      TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_recepcionista PRIMARY KEY (id_usuario),
    CONSTRAINT fk_recepcionista_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE ADMINISTRADOR (
    id_usuario  INT NOT NULL,
    cargo       VARCHAR(100) NOT NULL,
    activo      TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_administrador PRIMARY KEY (id_usuario),
    CONSTRAINT fk_administrador_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE MASCOTA (
    id_mascota       INT NOT NULL AUTO_INCREMENT,
    id_usuario       INT NOT NULL,
    nombre           VARCHAR(150) NOT NULL,
    fechaNacimiento  DATE NOT NULL,
    especie          VARCHAR(10) NOT NULL,
    raza             VARCHAR(150) NOT NULL,
    sexo             VARCHAR(10) NOT NULL,
    activo           TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_mascota PRIMARY KEY (id_mascota),
    KEY idx_mascota_cliente (id_usuario),
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (id_usuario)
        REFERENCES CLIENTE(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE HORARIO_ATENCION (
    id_horario      INT NOT NULL AUTO_INCREMENT,
    id_veterinario  INT NOT NULL,
    dia             VARCHAR(10) NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    activo          TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_horario_atencion PRIMARY KEY (id_horario),
    KEY idx_horario_veterinario (id_veterinario),
    CONSTRAINT fk_horario_veterinario FOREIGN KEY (id_veterinario)
        REFERENCES VETERINARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE CITA (
    id_cita             INT NOT NULL AUTO_INCREMENT,
    id_mascota          INT NOT NULL,
    tipo_atencion       VARCHAR(15) NOT NULL,
    fecha               DATE NOT NULL,
    hora                TIME NOT NULL,
    estado              VARCHAR(12) NOT NULL,
    motivo_cancelacion  VARCHAR(300) DEFAULT NULL,
    id_recepcionista    INT DEFAULT NULL,
    id_veterinario      INT DEFAULT NULL,
    activo              TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_cita PRIMARY KEY (id_cita),
    KEY idx_cita_mascota (id_mascota),
    KEY idx_cita_recepcionista (id_recepcionista),
    KEY idx_cita_veterinario (id_veterinario),
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota)
        REFERENCES MASCOTA(id_mascota) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cita_recepcionista FOREIGN KEY (id_recepcionista)
        REFERENCES RECEPCIONISTA(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario)
        REFERENCES VETERINARIO(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE ATENCION_MEDICA (
    id_atencion    INT NOT NULL AUTO_INCREMENT,
    observaciones  VARCHAR(255) DEFAULT NULL,
    hora_inicio    TIME DEFAULT NULL,
    hora_fin       TIME DEFAULT NULL,
    id_cita        INT NOT NULL,
    peso_actual    DOUBLE NOT NULL DEFAULT 0,
    alergias       VARCHAR(500) DEFAULT NULL,
    activo         TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_atencion_medica PRIMARY KEY (id_atencion),
    KEY idx_atencion_cita (id_cita),
    CONSTRAINT fk_atencion_cita FOREIGN KEY (id_cita)
        REFERENCES CITA(id_cita) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE CONSULTA (
    id_atencion    INT NOT NULL,
    motivoConsulta VARCHAR(400) NOT NULL,
    diagnostico    VARCHAR(400) NOT NULL,
    tratamiento    VARCHAR(400) NOT NULL,
    activo         TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_consulta PRIMARY KEY (id_atencion),
    CONSTRAINT fk_consulta_atencion FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE CONTROL (
    id_atencion   INT NOT NULL,
    evolucion     VARCHAR(500) NOT NULL,
    indicaciones  VARCHAR(500) NOT NULL,
    activo        TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_control PRIMARY KEY (id_atencion),
    CONSTRAINT fk_control_atencion FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE CIRUGIA (
    id_atencion                    INT NOT NULL,
    procedimiento                  VARCHAR(500) NOT NULL,
    indicaciones_post_operatorias  VARCHAR(500) NOT NULL,
    activo                         TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_cirugia PRIMARY KEY (id_atencion),
    CONSTRAINT fk_cirugia_atencion FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE VACUNACION (
    id_atencion        INT NOT NULL,
    fechaAplicacion    DATE NOT NULL,
    fechaProximaDosis  DATE NOT NULL,
    dosis              VARCHAR(200) NOT NULL,
    activo             TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_vacunacion PRIMARY KEY (id_atencion),
    CONSTRAINT fk_vacunacion_atencion FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE VACUNA (
    id_vacuna    INT NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(100) NOT NULL,
    descripcion  VARCHAR(500) NOT NULL,
    activo       TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_vacuna PRIMARY KEY (id_vacuna)
);

CREATE TABLE VACUNACION_VACUNA (
    id_vacunacion  INT NOT NULL,
    id_vacuna      INT NOT NULL,
    activo         TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_vacunacion_vacuna PRIMARY KEY (id_vacunacion,id_vacuna),
    KEY idx_vacunacion_vacuna_catalogo (id_vacuna),
    CONSTRAINT fk_vacunacion_vacuna_aplicacion FOREIGN KEY (id_vacunacion)
        REFERENCES VACUNACION(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_vacunacion_vacuna_catalogo FOREIGN KEY (id_vacuna)
        REFERENCES VACUNA(id_vacuna) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE RECETA (
    id_receta     INT NOT NULL AUTO_INCREMENT,
    id_atencion   INT NOT NULL,
    fecha         DATE NOT NULL,
    indicaciones  VARCHAR(600) NOT NULL,
    activo        TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_receta PRIMARY KEY (id_receta),
    CONSTRAINT uq_receta_atencion UNIQUE (id_atencion),
    CONSTRAINT fk_receta_atencion FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE MEDICAMENTO (
    id_medicamento     INT NOT NULL AUTO_INCREMENT,
    nombre             VARCHAR(100) NOT NULL,
    descripcion        VARCHAR(200) NOT NULL,
    fecha_vencimiento  DATE NOT NULL,
    monto              DOUBLE NOT NULL,
    activo             TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_medicamento PRIMARY KEY (id_medicamento)
);

CREATE TABLE DETALLE_RECETA (
    id_detalle_receta  INT NOT NULL AUTO_INCREMENT,
    id_receta          INT NOT NULL,
    id_medicamento     INT NOT NULL,
    dosis              VARCHAR(200) NOT NULL,
    frecuencia         VARCHAR(200) NOT NULL,
    duracion           VARCHAR(200) NOT NULL,
    montoTotal         DOUBLE DEFAULT NULL,
    activo             TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_detalle_receta PRIMARY KEY (id_detalle_receta),
    KEY idx_detalle_receta (id_receta),
    KEY idx_detalle_medicamento (id_medicamento),
    CONSTRAINT fk_detalle_receta FOREIGN KEY (id_receta)
        REFERENCES RECETA(id_receta) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_medicamento FOREIGN KEY (id_medicamento)
        REFERENCES MEDICAMENTO(id_medicamento) ON UPDATE CASCADE ON DELETE RESTRICT
);

DELIMITER $$

DROP PROCEDURE IF EXISTS INSERTAR_USUARIO_BASE$$
CREATE PROCEDURE INSERTAR_USUARIO_BASE(
    OUT _id_usuario INT,
    IN _dni VARCHAR(9), IN _nombre VARCHAR(50),
    IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50),
    IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN)
BEGIN
    INSERT INTO USUARIO(dni, nombre, apellido_paterno, apellido_materno,
                        nombre_usuario, contrasena, activo)
    VALUES(_dni, _nombre, _apellido_paterno, _apellido_materno,
           _nombre_usuario, _contrasena, _activo);
    SET _id_usuario = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_USUARIO_BASE$$
CREATE PROCEDURE MODIFICAR_USUARIO_BASE(
    OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50),
    IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50),
    IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255),
    IN _activo BOOLEAN, IN _id_usuario INT)
BEGIN
    UPDATE USUARIO
    SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno,
        apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario,
        contrasena=_contrasena, activo=_activo
    WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CIRUGIA$$
CREATE PROCEDURE INSERTAR_CIRUGIA(OUT _id_atencion INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _procedimiento VARCHAR(500), IN _indicaciones_post_operatorias VARCHAR(500))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM CITA
                   WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita debe estar confirmada';
    END IF;
INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_cita, peso_actual, alergias) VALUES (_observaciones, _hora_inicio, _hora_fin, _id_cita, _peso_actual, _alergias);
    SET _id_atencion = LAST_INSERT_ID();
    INSERT INTO CIRUGIA (id_atencion, procedimiento, indicaciones_post_operatorias) VALUES (_id_atencion, _procedimiento, _indicaciones_post_operatorias);
    UPDATE CITA SET estado = 'ATENDIDA'
    WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA';
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CIRUGIA$$
CREATE PROCEDURE MODIFICAR_CIRUGIA(OUT _resultado INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _id_atencion INT, IN _procedimiento VARCHAR(500), IN _indicaciones_post_operatorias VARCHAR(500))
BEGIN
UPDATE ATENCION_MEDICA SET observaciones=_observaciones, hora_inicio=_hora_inicio, hora_fin=_hora_fin, id_cita=_id_cita, peso_actual=_peso_actual, alergias=_alergias WHERE id_atencion=_id_atencion;
    UPDATE CIRUGIA SET procedimiento=_procedimiento, indicaciones_post_operatorias=_indicaciones_post_operatorias WHERE id_atencion=_id_atencion;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CIRUGIA$$
CREATE PROCEDURE ELIMINAR_CIRUGIA(OUT _resultado INT, IN _id_atencion INT)
BEGIN
UPDATE CIRUGIA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE ATENCION_MEDICA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CIRUGIA_X_ID$$
CREATE PROCEDURE LISTAR_CIRUGIA_X_ID(IN _id_atencion INT)
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.procedimiento, c.indicaciones_post_operatorias, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.id_atencion=_id_atencion AND a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CIRUGIA_TODOS$$
CREATE PROCEDURE LISTAR_CIRUGIA_TODOS()
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.procedimiento, c.indicaciones_post_operatorias, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CIRUGIA c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CONSULTA$$
CREATE PROCEDURE INSERTAR_CONSULTA(OUT _id_atencion INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _motivoconsulta VARCHAR(400), IN _diagnostico VARCHAR(400), IN _tratamiento VARCHAR(400))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM CITA
                   WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita debe estar confirmada';
    END IF;
INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_cita, peso_actual, alergias) VALUES (_observaciones, _hora_inicio, _hora_fin, _id_cita, _peso_actual, _alergias);
    SET _id_atencion = LAST_INSERT_ID();
    INSERT INTO CONSULTA (id_atencion, motivoConsulta, diagnostico, tratamiento) VALUES (_id_atencion, _motivoconsulta, _diagnostico, _tratamiento);
    UPDATE CITA SET estado = 'ATENDIDA'
    WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA';
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CONSULTA$$
CREATE PROCEDURE MODIFICAR_CONSULTA(OUT _resultado INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _id_atencion INT, IN _motivoconsulta VARCHAR(400), IN _diagnostico VARCHAR(400), IN _tratamiento VARCHAR(400))
BEGIN
UPDATE ATENCION_MEDICA SET observaciones=_observaciones, hora_inicio=_hora_inicio, hora_fin=_hora_fin, id_cita=_id_cita, peso_actual=_peso_actual, alergias=_alergias WHERE id_atencion=_id_atencion;
    UPDATE CONSULTA SET motivoConsulta=_motivoconsulta, diagnostico=_diagnostico, tratamiento=_tratamiento WHERE id_atencion=_id_atencion;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CONSULTA$$
CREATE PROCEDURE ELIMINAR_CONSULTA(OUT _resultado INT, IN _id_atencion INT)
BEGIN
UPDATE CONSULTA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE ATENCION_MEDICA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CONSULTA_X_ID$$
CREATE PROCEDURE LISTAR_CONSULTA_X_ID(IN _id_atencion INT)
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.motivoConsulta, c.diagnostico, c.tratamiento, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.id_atencion=_id_atencion AND a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CONSULTA_TODOS$$
CREATE PROCEDURE LISTAR_CONSULTA_TODOS()
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.motivoConsulta, c.diagnostico, c.tratamiento, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CONSULTA c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CONTROL$$
CREATE PROCEDURE INSERTAR_CONTROL(OUT _id_atencion INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _evolucion VARCHAR(500), IN _indicaciones VARCHAR(500))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM CITA
                   WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita debe estar confirmada';
    END IF;
INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_cita, peso_actual, alergias) VALUES (_observaciones, _hora_inicio, _hora_fin, _id_cita, _peso_actual, _alergias);
    SET _id_atencion = LAST_INSERT_ID();
    INSERT INTO CONTROL (id_atencion, evolucion, indicaciones) VALUES (_id_atencion, _evolucion, _indicaciones);
    UPDATE CITA SET estado = 'ATENDIDA'
    WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA';
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CONTROL$$
CREATE PROCEDURE MODIFICAR_CONTROL(OUT _resultado INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _id_atencion INT, IN _evolucion VARCHAR(500), IN _indicaciones VARCHAR(500))
BEGIN
UPDATE ATENCION_MEDICA SET observaciones=_observaciones, hora_inicio=_hora_inicio, hora_fin=_hora_fin, id_cita=_id_cita, peso_actual=_peso_actual, alergias=_alergias WHERE id_atencion=_id_atencion;
    UPDATE CONTROL SET evolucion=_evolucion, indicaciones=_indicaciones WHERE id_atencion=_id_atencion;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CONTROL$$
CREATE PROCEDURE ELIMINAR_CONTROL(OUT _resultado INT, IN _id_atencion INT)
BEGIN
UPDATE CONTROL SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE ATENCION_MEDICA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CONTROL_X_ID$$
CREATE PROCEDURE LISTAR_CONTROL_X_ID(IN _id_atencion INT)
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.evolucion, c.indicaciones, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.id_atencion=_id_atencion AND a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CONTROL_TODOS$$
CREATE PROCEDURE LISTAR_CONTROL_TODOS()
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, c.evolucion, c.indicaciones, r.id_receta FROM ATENCION_MEDICA a INNER JOIN CONTROL c ON a.id_atencion=c.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_VACUNA$$
CREATE PROCEDURE INSERTAR_VACUNA(OUT _id_vacuna INT, IN _nombre VARCHAR(100), IN _descripcion VARCHAR(500), IN _activo BOOLEAN)
BEGIN
INSERT INTO VACUNA (nombre, descripcion, activo) VALUES (_nombre, _descripcion, _activo);
    SET _id_vacuna = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_VACUNA$$
CREATE PROCEDURE MODIFICAR_VACUNA(OUT _resultado INT, IN _nombre VARCHAR(100), IN _descripcion VARCHAR(500), IN _id_vacuna INT)
BEGIN
UPDATE VACUNA SET nombre=_nombre, descripcion=_descripcion WHERE id_vacuna=_id_vacuna AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_VACUNA$$
CREATE PROCEDURE ELIMINAR_VACUNA(OUT _resultado INT, IN _id_vacuna INT)
BEGIN
UPDATE VACUNA SET activo=0 WHERE id_vacuna=_id_vacuna AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_VACUNA_X_ID$$
CREATE PROCEDURE LISTAR_VACUNA_X_ID(IN _id_vacuna INT)
BEGIN
SELECT id_vacuna, nombre, descripcion, activo FROM VACUNA WHERE id_vacuna=_id_vacuna AND activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_VACUNA_TODOS$$
CREATE PROCEDURE LISTAR_VACUNA_TODOS()
BEGIN
SELECT id_vacuna, nombre, descripcion, activo FROM VACUNA WHERE activo=1;
END$$

DROP PROCEDURE IF EXISTS ASOCIARVACUNA_VACUNACION$$
CREATE PROCEDURE ASOCIARVACUNA_VACUNACION(OUT _resultado INT, IN _id_vacunacion INT, IN _id_vacuna INT)
BEGIN
INSERT INTO VACUNACION_VACUNA (id_vacunacion, id_vacuna, activo) VALUES (_id_vacunacion, _id_vacuna, 1);
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS CARGARVACUNAS_VACUNACION$$
CREATE PROCEDURE CARGARVACUNAS_VACUNACION(IN _id_vacunacion INT)
BEGIN
SELECT v.id_vacuna, v.nombre, v.descripcion FROM VACUNACION_VACUNA vv INNER JOIN VACUNA v ON vv.id_vacuna=v.id_vacuna WHERE vv.id_vacunacion=_id_vacunacion AND vv.activo=1 AND v.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_VACUNACION$$
CREATE PROCEDURE INSERTAR_VACUNACION(OUT _id_atencion INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _fechaaplicacion DATE, IN _fechaproximadosis DATE, IN _dosis VARCHAR(200))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM CITA
                   WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita debe estar confirmada';
    END IF;
INSERT INTO ATENCION_MEDICA (observaciones, hora_inicio, hora_fin, id_cita, peso_actual, alergias) VALUES (_observaciones, _hora_inicio, _hora_fin, _id_cita, _peso_actual, _alergias);
    SET _id_atencion = LAST_INSERT_ID();
    INSERT INTO VACUNACION (id_atencion, fechaAplicacion, fechaProximaDosis, dosis) VALUES (_id_atencion, _fechaaplicacion, _fechaproximadosis, _dosis);
    UPDATE CITA SET estado = 'ATENDIDA'
    WHERE id_cita = _id_cita AND activo = 1 AND estado = 'CONFIRMADA';
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_VACUNACION$$
CREATE PROCEDURE MODIFICAR_VACUNACION(OUT _resultado INT, IN _observaciones VARCHAR(255), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_cita INT, IN _peso_actual DOUBLE, IN _alergias VARCHAR(500), IN _id_atencion INT, IN _fechaaplicacion DATE, IN _fechaproximadosis DATE, IN _dosis VARCHAR(200))
BEGIN
UPDATE ATENCION_MEDICA SET observaciones=_observaciones, hora_inicio=_hora_inicio, hora_fin=_hora_fin, id_cita=_id_cita, peso_actual=_peso_actual, alergias=_alergias WHERE id_atencion=_id_atencion;
    UPDATE VACUNACION SET fechaAplicacion=_fechaaplicacion, fechaProximaDosis=_fechaproximadosis, dosis=_dosis WHERE id_atencion=_id_atencion;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_VACUNACION$$
CREATE PROCEDURE ELIMINAR_VACUNACION(OUT _resultado INT, IN _id_atencion INT)
BEGIN
UPDATE VACUNACION SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
    SET _resultado = ROW_COUNT();
    IF _resultado > 0 THEN
        UPDATE VACUNACION_VACUNA SET activo=0 WHERE id_vacunacion=_id_atencion AND activo=1;
    END IF;
    IF _resultado > 0 THEN
        UPDATE ATENCION_MEDICA SET activo=0 WHERE id_atencion=_id_atencion AND activo=1;
        IF ROW_COUNT() = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se pudo inactivar el registro base';
        END IF;
    END IF;
END$$

DROP PROCEDURE IF EXISTS LISTAR_VACUNACION_X_ID$$
CREATE PROCEDURE LISTAR_VACUNACION_X_ID(IN _id_atencion INT)
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, v.fechaAplicacion, v.fechaProximaDosis, v.dosis, r.id_receta FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion=v.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.id_atencion=_id_atencion AND a.activo=1 AND v.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_VACUNACION_TODOS$$
CREATE PROCEDURE LISTAR_VACUNACION_TODOS()
BEGIN
SELECT a.id_atencion, a.observaciones, a.hora_inicio, a.hora_fin, a.id_cita, a.peso_actual, a.alergias, v.fechaAplicacion, v.fechaProximaDosis, v.dosis, r.id_receta FROM ATENCION_MEDICA a INNER JOIN VACUNACION v ON a.id_atencion=v.id_atencion LEFT JOIN RECETA r ON a.id_atencion=r.id_atencion AND r.activo=1 WHERE a.activo=1 AND v.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CITA$$
CREATE PROCEDURE INSERTAR_CITA(OUT _id_cita INT, IN _id_mascota INT, IN _tipo_atencion VARCHAR(15), IN _fecha DATE, IN _hora TIME, IN _estado VARCHAR(12), IN _motivo_cancelacion VARCHAR(300), IN _id_recepcionista INT, IN _id_veterinario INT, IN _activo BOOLEAN)
BEGIN
INSERT INTO CITA (id_mascota, tipo_atencion, fecha, hora, estado, motivo_cancelacion, id_recepcionista, id_veterinario, activo) VALUES (_id_mascota, _tipo_atencion, _fecha, _hora, _estado, _motivo_cancelacion, _id_recepcionista, _id_veterinario, _activo);
    SET _id_cita = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CITA$$
CREATE PROCEDURE MODIFICAR_CITA(OUT _resultado INT, IN _id_mascota INT, IN _tipo_atencion VARCHAR(15), IN _fecha DATE, IN _hora TIME, IN _estado VARCHAR(12), IN _motivo_cancelacion VARCHAR(300), IN _id_recepcionista INT, IN _id_veterinario INT, IN _activo BOOLEAN, IN _id_cita INT)
BEGIN
UPDATE CITA SET id_mascota=_id_mascota, tipo_atencion=_tipo_atencion, fecha=_fecha, hora=_hora, estado=_estado, motivo_cancelacion=_motivo_cancelacion, id_recepcionista=_id_recepcionista, id_veterinario=_id_veterinario, activo=_activo WHERE id_cita=_id_cita AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CITA$$
CREATE PROCEDURE ELIMINAR_CITA(OUT _resultado INT, IN _id_cita INT)
BEGIN
UPDATE CITA SET activo=0 WHERE id_cita=_id_cita AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_CITA_X_ID$$
CREATE PROCEDURE LISTAR_CITA_X_ID(IN _id_cita INT)
BEGIN
SELECT c.*, m.id_usuario AS id_usuario_cliente, m.activo AS mascota_activa, cl.activo AS cliente_activo FROM CITA c INNER JOIN MASCOTA m ON c.id_mascota=m.id_mascota INNER JOIN CLIENTE cl ON m.id_usuario=cl.id_usuario WHERE c.id_cita=_id_cita AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CITA_TODOS$$
CREATE PROCEDURE LISTAR_CITA_TODOS()
BEGIN
SELECT c.*, m.id_usuario AS id_usuario_cliente, m.activo AS mascota_activa, cl.activo AS cliente_activo FROM CITA c INNER JOIN MASCOTA m ON c.id_mascota=m.id_mascota INNER JOIN CLIENTE cl ON m.id_usuario=cl.id_usuario WHERE c.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_HORARIOATENCION$$
CREATE PROCEDURE INSERTAR_HORARIOATENCION(OUT _id_horario_atencion INT, IN _id_veterinario INT, IN _dia VARCHAR(10), IN _hora_inicio TIME, IN _hora_fin TIME)
BEGIN
INSERT INTO HORARIO_ATENCION (id_veterinario, dia, hora_inicio, hora_fin) VALUES (_id_veterinario, _dia, _hora_inicio, _hora_fin);
    SET _id_horario_atencion = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_HORARIOATENCION$$
CREATE PROCEDURE MODIFICAR_HORARIOATENCION(OUT _resultado INT, IN _id_veterinario INT, IN _dia VARCHAR(10), IN _hora_inicio TIME, IN _hora_fin TIME, IN _id_horario INT)
BEGIN
UPDATE HORARIO_ATENCION SET id_veterinario=_id_veterinario, dia=_dia, hora_inicio=_hora_inicio, hora_fin=_hora_fin WHERE id_horario=_id_horario;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_HORARIOATENCION$$
CREATE PROCEDURE ELIMINAR_HORARIOATENCION(OUT _resultado INT, IN _id_horario INT)
BEGIN
UPDATE HORARIO_ATENCION SET activo=0 WHERE id_horario=_id_horario AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_HORARIOATENCION_X_ID$$
CREATE PROCEDURE LISTAR_HORARIOATENCION_X_ID(IN _id_horario INT)
BEGIN
SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION WHERE id_horario=_id_horario AND activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_HORARIOATENCION_TODOS$$
CREATE PROCEDURE LISTAR_HORARIOATENCION_TODOS()
BEGIN
SELECT id_horario, id_veterinario, dia, hora_inicio, hora_fin FROM HORARIO_ATENCION WHERE activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_MASCOTA$$
CREATE PROCEDURE INSERTAR_MASCOTA(OUT _id_mascota INT, IN _id_usuario INT, IN _nombre VARCHAR(150), IN _fechanacimiento DATE, IN _especie VARCHAR(10), IN _raza VARCHAR(150), IN _sexo VARCHAR(10), IN _activo BOOLEAN)
BEGIN
INSERT INTO MASCOTA (id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo) VALUES (_id_usuario, _nombre, _fechanacimiento, _especie, _raza, _sexo, _activo);
    SET _id_mascota = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_MASCOTA$$
CREATE PROCEDURE MODIFICAR_MASCOTA(OUT _resultado INT, IN _id_usuario INT, IN _nombre VARCHAR(150), IN _fechanacimiento DATE, IN _especie VARCHAR(10), IN _raza VARCHAR(150), IN _sexo VARCHAR(10), IN _activo BOOLEAN, IN _id_mascota INT)
BEGIN
UPDATE MASCOTA SET id_usuario=_id_usuario, nombre=_nombre, fechaNacimiento=_fechanacimiento, especie=_especie, raza=_raza, sexo=_sexo, activo=_activo WHERE id_mascota=_id_mascota;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_MASCOTA$$
CREATE PROCEDURE ELIMINAR_MASCOTA(OUT _resultado INT, IN _id_mascota INT)
BEGIN
UPDATE MASCOTA SET activo=0 WHERE id_mascota=_id_mascota;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_MASCOTA_X_ID$$
CREATE PROCEDURE LISTAR_MASCOTA_X_ID(IN _id_mascota INT)
BEGIN
SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE id_mascota=_id_mascota AND activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_MASCOTA_TODOS$$
CREATE PROCEDURE LISTAR_MASCOTA_TODOS()
BEGIN
SELECT id_mascota, id_usuario, nombre, fechaNacimiento, especie, raza, sexo, activo FROM MASCOTA WHERE activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_DETALLERECETA$$
CREATE PROCEDURE INSERTAR_DETALLERECETA(OUT _id_detalle_receta INT, IN _id_receta INT, IN _id_medicamento INT, IN _dosis VARCHAR(200), IN _frecuencia VARCHAR(200), IN _duracion VARCHAR(200), IN _montototal DOUBLE)
BEGIN
INSERT INTO DETALLE_RECETA (id_receta, id_medicamento, dosis, frecuencia, duracion, montoTotal) VALUES (_id_receta, _id_medicamento, _dosis, _frecuencia, _duracion, _montototal);
    SET _id_detalle_receta = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLERECETA$$
CREATE PROCEDURE MODIFICAR_DETALLERECETA(OUT _resultado INT, IN _id_receta INT, IN _id_medicamento INT, IN _dosis VARCHAR(200), IN _frecuencia VARCHAR(200), IN _duracion VARCHAR(200), IN _montototal DOUBLE, IN _id_detalle_receta INT)
BEGIN
UPDATE DETALLE_RECETA SET id_receta=_id_receta, id_medicamento=_id_medicamento, dosis=_dosis, frecuencia=_frecuencia, duracion=_duracion, montoTotal=_montototal WHERE id_detalle_receta=_id_detalle_receta;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLERECETA$$
CREATE PROCEDURE ELIMINAR_DETALLERECETA(OUT _resultado INT, IN _id_detalle_receta INT)
BEGIN
UPDATE DETALLE_RECETA SET activo=0 WHERE id_detalle_receta=_id_detalle_receta AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_DETALLERECETA_X_ID$$
CREATE PROCEDURE LISTAR_DETALLERECETA_X_ID(IN _id_detalle_receta INT)
BEGIN
SELECT d.id_detalle_receta, d.id_receta, d.id_medicamento, d.dosis, d.frecuencia, d.duracion, d.montoTotal, m.nombre, m.descripcion, m.fecha_vencimiento, m.monto FROM DETALLE_RECETA d INNER JOIN MEDICAMENTO m ON d.id_medicamento=m.id_medicamento WHERE d.id_detalle_receta=_id_detalle_receta AND d.activo=1 AND m.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_DETALLERECETA_TODOS$$
CREATE PROCEDURE LISTAR_DETALLERECETA_TODOS()
BEGIN
SELECT d.id_detalle_receta, d.id_receta, d.id_medicamento, d.dosis, d.frecuencia, d.duracion, d.montoTotal, m.nombre, m.descripcion, m.fecha_vencimiento, m.monto FROM DETALLE_RECETA d INNER JOIN MEDICAMENTO m ON d.id_medicamento=m.id_medicamento WHERE d.activo=1 AND m.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_MEDICAMENTO$$
CREATE PROCEDURE INSERTAR_MEDICAMENTO(OUT _id_medicamento INT, IN _nombre VARCHAR(100), IN _descripcion VARCHAR(200), IN _fecha_vencimiento DATE, IN _monto DOUBLE)
BEGIN
INSERT INTO MEDICAMENTO (nombre, descripcion, fecha_vencimiento, monto) VALUES (_nombre, _descripcion, _fecha_vencimiento, _monto);
    SET _id_medicamento = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_MEDICAMENTO$$
CREATE PROCEDURE MODIFICAR_MEDICAMENTO(OUT _resultado INT, IN _nombre VARCHAR(100), IN _descripcion VARCHAR(200), IN _fecha_vencimiento DATE, IN _monto DOUBLE, IN _id_medicamento INT)
BEGIN
UPDATE MEDICAMENTO SET nombre=_nombre, descripcion=_descripcion, fecha_vencimiento=_fecha_vencimiento, monto=_monto WHERE id_medicamento=_id_medicamento AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_MEDICAMENTO$$
CREATE PROCEDURE ELIMINAR_MEDICAMENTO(OUT _resultado INT, IN _id_medicamento INT)
BEGIN
UPDATE MEDICAMENTO SET activo=0 WHERE id_medicamento=_id_medicamento AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_MEDICAMENTO_X_ID$$
CREATE PROCEDURE LISTAR_MEDICAMENTO_X_ID(IN _id_medicamento INT)
BEGIN
SELECT id_medicamento, nombre, descripcion, fecha_vencimiento, monto, activo FROM MEDICAMENTO WHERE id_medicamento=_id_medicamento AND activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_MEDICAMENTO_TODOS$$
CREATE PROCEDURE LISTAR_MEDICAMENTO_TODOS()
BEGIN
SELECT id_medicamento, nombre, descripcion, fecha_vencimiento, monto, activo FROM MEDICAMENTO WHERE activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_RECETA$$
CREATE PROCEDURE INSERTAR_RECETA(OUT _id_receta INT, IN _id_atencion INT, IN _fecha DATE, IN _indicaciones VARCHAR(600))
BEGIN
INSERT INTO RECETA (id_atencion, fecha, indicaciones) VALUES (_id_atencion, _fecha, _indicaciones);
    SET _id_receta = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_RECETA$$
CREATE PROCEDURE MODIFICAR_RECETA(OUT _resultado INT, IN _id_atencion INT, IN _fecha DATE, IN _indicaciones VARCHAR(600), IN _id_receta INT)
BEGIN
UPDATE RECETA SET id_atencion=_id_atencion, fecha=_fecha, indicaciones=_indicaciones WHERE id_receta=_id_receta;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_RECETA$$
CREATE PROCEDURE ELIMINAR_RECETA(OUT _resultado INT, IN _id_receta INT)
BEGIN
UPDATE RECETA SET activo=0 WHERE id_receta=_id_receta AND activo=1;
    SET _resultado = ROW_COUNT();
END$$

DROP PROCEDURE IF EXISTS LISTAR_RECETA_X_ID$$
CREATE PROCEDURE LISTAR_RECETA_X_ID(IN _id_receta INT)
BEGIN
SELECT r.id_receta, r.id_atencion, r.fecha, r.indicaciones, co.id_atencion AS id_consulta, ct.id_atencion AS id_control, ci.id_atencion AS id_cirugia, va.id_atencion AS id_vacunacion FROM RECETA r LEFT JOIN CONSULTA co ON r.id_atencion=co.id_atencion AND co.activo=1 LEFT JOIN CONTROL ct ON r.id_atencion=ct.id_atencion AND ct.activo=1 LEFT JOIN CIRUGIA ci ON r.id_atencion=ci.id_atencion AND ci.activo=1 LEFT JOIN VACUNACION va ON r.id_atencion=va.id_atencion AND va.activo=1 WHERE r.id_receta=_id_receta AND r.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_RECETA_TODOS$$
CREATE PROCEDURE LISTAR_RECETA_TODOS()
BEGIN
SELECT r.id_receta, r.id_atencion, r.fecha, r.indicaciones, co.id_atencion AS id_consulta, ct.id_atencion AS id_control, ci.id_atencion AS id_cirugia, va.id_atencion AS id_vacunacion FROM RECETA r LEFT JOIN CONSULTA co ON r.id_atencion=co.id_atencion AND co.activo=1 LEFT JOIN CONTROL ct ON r.id_atencion=ct.id_atencion AND ct.activo=1 LEFT JOIN CIRUGIA ci ON r.id_atencion=ci.id_atencion AND ci.activo=1 LEFT JOIN VACUNACION va ON r.id_atencion=va.id_atencion AND va.activo=1 WHERE r.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_ADMINISTRADOR$$
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _cargo VARCHAR(100))
BEGIN
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, _contrasena, _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO ADMINISTRADOR (id_usuario, cargo) VALUES (_id_usuario, _cargo);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_ADMINISTRADOR$$
CREATE PROCEDURE MODIFICAR_ADMINISTRADOR(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _cargo VARCHAR(100))
BEGIN
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena=_contrasena, activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE ADMINISTRADOR SET cargo=_cargo WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
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
END$$

DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADOR_X_ID$$
CREATE PROCEDURE LISTAR_ADMINISTRADOR_X_ID(IN _id_usuario INT)
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, a.cargo FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario WHERE u.id_usuario=_id_usuario AND u.activo=1 AND a.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADOR_TODOS$$
CREATE PROCEDURE LISTAR_ADMINISTRADOR_TODOS()
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, a.cargo FROM USUARIO u INNER JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario WHERE u.activo=1 AND a.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_CLIENTE$$
CREATE PROCEDURE INSERTAR_CLIENTE(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, _contrasena, _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO CLIENTE (id_usuario, correo, telefono) VALUES (_id_usuario, _correo, _telefono);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CLIENTE$$
CREATE PROCEDURE MODIFICAR_CLIENTE(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena=_contrasena, activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE CLIENTE SET correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
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
END$$

DROP PROCEDURE IF EXISTS LISTAR_CLIENTE_X_ID$$
CREATE PROCEDURE LISTAR_CLIENTE_X_ID(IN _id_usuario INT)
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario=c.id_usuario WHERE u.id_usuario=_id_usuario AND u.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CLIENTE_TODOS$$
CREATE PROCEDURE LISTAR_CLIENTE_TODOS()
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono FROM USUARIO u INNER JOIN CLIENTE c ON u.id_usuario=c.id_usuario WHERE u.activo=1 AND c.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_RECEPCIONISTA$$
CREATE PROCEDURE INSERTAR_RECEPCIONISTA(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _turno VARCHAR(150), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, _contrasena, _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO RECEPCIONISTA (id_usuario, turno, correo, telefono) VALUES (_id_usuario, _turno, _correo, _telefono);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_RECEPCIONISTA$$
CREATE PROCEDURE MODIFICAR_RECEPCIONISTA(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _turno VARCHAR(150), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena=_contrasena, activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE RECEPCIONISTA SET turno=_turno, correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
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
END$$

DROP PROCEDURE IF EXISTS LISTAR_RECEPCIONISTA_X_ID$$
CREATE PROCEDURE LISTAR_RECEPCIONISTA_X_ID(IN _id_usuario INT)
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, r.turno, r.correo, r.telefono FROM USUARIO u INNER JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario WHERE u.id_usuario=_id_usuario AND u.activo=1 AND r.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_RECEPCIONISTA_TODOS$$
CREATE PROCEDURE LISTAR_RECEPCIONISTA_TODOS()
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, r.turno, r.correo, r.telefono FROM USUARIO u INNER JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario WHERE u.activo=1 AND r.activo=1;
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
END$$

DROP PROCEDURE IF EXISTS LISTAR_USUARIO_X_ID$$
CREATE PROCEDURE LISTAR_USUARIO_X_ID(IN _id_usuario INT)
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono, v.num_colegiatura, v.especialidad, r.turno, a.cargo, c.id_usuario AS id_cliente, v.id_usuario AS id_veterinario, r.id_usuario AS id_recepcionista, a.id_usuario AS id_administrador FROM USUARIO u LEFT JOIN CLIENTE c ON u.id_usuario=c.id_usuario AND c.activo=1 LEFT JOIN VETERINARIO v ON u.id_usuario=v.id_usuario AND v.activo=1 LEFT JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario AND r.activo=1 LEFT JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario AND a.activo=1 WHERE u.id_usuario=_id_usuario AND u.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_USUARIO_TODOS$$
CREATE PROCEDURE LISTAR_USUARIO_TODOS()
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, c.correo, c.telefono, v.num_colegiatura, v.especialidad, r.turno, a.cargo, c.id_usuario AS id_cliente, v.id_usuario AS id_veterinario, r.id_usuario AS id_recepcionista, a.id_usuario AS id_administrador FROM USUARIO u LEFT JOIN CLIENTE c ON u.id_usuario=c.id_usuario AND c.activo=1 LEFT JOIN VETERINARIO v ON u.id_usuario=v.id_usuario AND v.activo=1 LEFT JOIN RECEPCIONISTA r ON u.id_usuario=r.id_usuario AND r.activo=1 LEFT JOIN ADMINISTRADOR a ON u.id_usuario=a.id_usuario AND a.activo=1 WHERE u.activo=1;
END$$

DROP PROCEDURE IF EXISTS INSERTAR_VETERINARIO$$
CREATE PROCEDURE INSERTAR_VETERINARIO(OUT _id_usuario INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _num_colegiatura VARCHAR(10), IN _especialidad VARCHAR(20), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo) VALUES (_dni, _nombre, _apellido_paterno, _apellido_materno, _nombre_usuario, _contrasena, _activo);
    SET _id_usuario = LAST_INSERT_ID();
    INSERT INTO VETERINARIO (id_usuario, num_colegiatura, especialidad, correo, telefono) VALUES (_id_usuario, _num_colegiatura, _especialidad, _correo, _telefono);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_VETERINARIO$$
CREATE PROCEDURE MODIFICAR_VETERINARIO(OUT _resultado INT, IN _dni VARCHAR(9), IN _nombre VARCHAR(50), IN _apellido_paterno VARCHAR(50), IN _apellido_materno VARCHAR(50), IN _nombre_usuario VARCHAR(100), IN _contrasena VARCHAR(255), IN _activo BOOLEAN, IN _id_usuario INT, IN _num_colegiatura VARCHAR(10), IN _especialidad VARCHAR(20), IN _correo VARCHAR(200), IN _telefono VARCHAR(20))
BEGIN
UPDATE USUARIO SET dni=_dni, nombre=_nombre, apellido_paterno=_apellido_paterno, apellido_materno=_apellido_materno, nombre_usuario=_nombre_usuario, contrasena=_contrasena, activo=_activo WHERE id_usuario=_id_usuario;
    UPDATE VETERINARIO SET num_colegiatura=_num_colegiatura, especialidad=_especialidad, correo=_correo, telefono=_telefono WHERE id_usuario=_id_usuario;
    SET _resultado = ROW_COUNT();
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
END$$

DROP PROCEDURE IF EXISTS LISTAR_VETERINARIO_X_ID$$
CREATE PROCEDURE LISTAR_VETERINARIO_X_ID(IN _id_usuario INT)
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, v.num_colegiatura, v.especialidad, v.correo, v.telefono FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario=v.id_usuario WHERE u.id_usuario=_id_usuario AND u.activo=1 AND v.activo=1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_VETERINARIO_TODOS$$
CREATE PROCEDURE LISTAR_VETERINARIO_TODOS()
BEGIN
SELECT u.id_usuario, u.dni, u.nombre, u.apellido_paterno, u.apellido_materno, u.nombre_usuario, u.contrasena, u.activo, v.num_colegiatura, v.especialidad, v.correo, v.telefono FROM USUARIO u INNER JOIN VETERINARIO v ON u.id_usuario=v.id_usuario WHERE u.activo=1 AND v.activo=1;
END$$

DELIMITER ;


-- ===== INICIO DE LOS NUEVOS DATOS DE PRUEBA (SIN CAMBIOS AL ORIGINAL) =====
-- ============================================================
-- MASCOVET / DATOS DE PRUEBA PARA INTEGRACION JAVA REST + C#
-- Uso: ejecutar DESPUES del SQL original mascovetF.sql.
-- NO crea ni modifica tablas, procedimientos ni vistas.
-- Reejecutable: evita insertar otra vez los registros de muestra.
-- ADVERTENCIA: todos los nombres y usuarios son ficticios;
-- credenciales de demostracion: NO usar en produccion.
-- ============================================================
USE mascovetd;
START TRANSACTION;

-- 1. USUARIOS DEL SISTEMA (identificadores locales y ficticios)
INSERT INTO USUARIO (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo)
SELECT s.dni, s.nombre, s.apellido_paterno, s.apellido_materno, s.nombre_usuario, 'demo1234', 1
FROM (
    SELECT '99001001' AS dni, 'Ana' AS nombre, 'Rojas' AS apellido_paterno, 'Castillo' AS apellido_materno, 'prueba_cliente_ana' AS nombre_usuario
    UNION ALL SELECT '99001002', 'Bruno', 'Mendoza', 'Reyes', 'prueba_cliente_bruno'
    UNION ALL SELECT '99001003', 'Carla', 'Paredes', 'Vega', 'prueba_cliente_carla'
    UNION ALL SELECT '99001004', 'Luis', 'Salazar', 'Torres', 'prueba_veterinario_luis'
    UNION ALL SELECT '99001005', 'Elena', 'Campos', 'Flores', 'prueba_veterinario_elena'
    UNION ALL SELECT '99001006', 'Mario', 'Benavides', 'Ramos', 'prueba_veterinario_mario'
    UNION ALL SELECT '99001007', 'Diana', 'Huaman', 'Luna', 'prueba_recepcionista_diana'
    UNION ALL SELECT '99001008', 'Jorge', 'Delgado', 'Paz', 'prueba_recepcionista_jorge'
    UNION ALL SELECT '99001009', 'Sofia', 'Leon', 'Navarro', 'prueba_administrador_sofia'
) s
WHERE NOT EXISTS (SELECT 1 FROM USUARIO u WHERE u.nombre_usuario=s.nombre_usuario OR u.dni=s.dni);

-- 2. ROLES: CLIENTES
INSERT INTO CLIENTE (id_usuario, correo, telefono, activo)
SELECT u.id_usuario,
       CASE u.nombre_usuario
           WHEN 'prueba_cliente_ana' THEN 'ana.prueba@mascovet.example'
           WHEN 'prueba_cliente_bruno' THEN 'bruno.prueba@mascovet.example'
           ELSE 'carla.prueba@mascovet.example' END,
       CASE u.nombre_usuario
           WHEN 'prueba_cliente_ana' THEN '900001001'
           WHEN 'prueba_cliente_bruno' THEN '900001002'
           ELSE '900001003' END,
       1
FROM USUARIO u
WHERE u.nombre_usuario IN ('prueba_cliente_ana','prueba_cliente_bruno','prueba_cliente_carla')
AND NOT EXISTS (SELECT 1 FROM CLIENTE c WHERE c.id_usuario=u.id_usuario);

-- 3. ROLES: VETERINARIOS (especialidades validas del dominio)
INSERT INTO VETERINARIO (id_usuario, num_colegiatura, especialidad, correo, telefono, activo)
SELECT u.id_usuario,
       CASE u.nombre_usuario WHEN 'prueba_veterinario_luis' THEN 'CMVP9001'
           WHEN 'prueba_veterinario_elena' THEN 'CMVP9002' ELSE 'CMVP9003' END,
       CASE u.nombre_usuario WHEN 'prueba_veterinario_luis' THEN 'MEDICINA_GENERAL'
           WHEN 'prueba_veterinario_elena' THEN 'CIRUGIA' ELSE 'DERMATOLOGIA' END,
       CASE u.nombre_usuario WHEN 'prueba_veterinario_luis' THEN 'luis.prueba@mascovet.example'
           WHEN 'prueba_veterinario_elena' THEN 'elena.prueba@mascovet.example'
           ELSE 'mario.prueba@mascovet.example' END,
       CASE u.nombre_usuario WHEN 'prueba_veterinario_luis' THEN '900002001'
           WHEN 'prueba_veterinario_elena' THEN '900002002' ELSE '900002003' END,
       1
FROM USUARIO u
WHERE u.nombre_usuario IN ('prueba_veterinario_luis','prueba_veterinario_elena','prueba_veterinario_mario')
AND NOT EXISTS (SELECT 1 FROM VETERINARIO v WHERE v.id_usuario=u.id_usuario);

-- 4. ROLES: RECEPCIONISTAS Y ADMINISTRADOR
INSERT INTO RECEPCIONISTA (id_usuario, turno, correo, telefono, activo)
SELECT u.id_usuario,
       CASE WHEN u.nombre_usuario='prueba_recepcionista_diana' THEN 'MAÑANA' ELSE 'TARDE' END,
       CASE WHEN u.nombre_usuario='prueba_recepcionista_diana' THEN 'diana.prueba@mascovet.example'
           ELSE 'jorge.prueba@mascovet.example' END,
       CASE WHEN u.nombre_usuario='prueba_recepcionista_diana' THEN '900003001' ELSE '900003002' END, 1
FROM USUARIO u
WHERE u.nombre_usuario IN ('prueba_recepcionista_diana','prueba_recepcionista_jorge')
AND NOT EXISTS (SELECT 1 FROM RECEPCIONISTA r WHERE r.id_usuario=u.id_usuario);

INSERT INTO ADMINISTRADOR (id_usuario, cargo, activo)
SELECT u.id_usuario, 'ADMINISTRADOR', 1 FROM USUARIO u
WHERE u.nombre_usuario='prueba_administrador_sofia'
AND NOT EXISTS (SELECT 1 FROM ADMINISTRADOR a WHERE a.id_usuario=u.id_usuario);

-- 5. MASCOTAS: cubre fichas por cliente, especies y sexos distintos
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

-- 6. HORARIOS SEMANALES: LUNES A VIERNES, y un SABADO
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

-- 7. CITAS HISTORICAS Y FUTURAS PARA RESERVA, CONFIRMACION Y CANCELACION
-- Fecha de referencia para estas pruebas: noviembre de 2026.
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

-- 8. ATENCIONES MEDICAS: CONSULTA, CONTROL, CIRUGIA Y VACUNACION
-- Identifica citas por propietario/mascota/fecha (sin asumir AUTOINCREMENT).
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

-- 9. SUBTIPOS DE ATENCION (coinciden con el tipo de cada CITA)
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

-- 10. CATALOGO DE VACUNAS
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

-- 11. CATALOGO DE MEDICAMENTOS
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

-- 12. RECETAS: una por atencion (restriccion uq_receta_atencion)
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

-- 13. COMPROBACION: conteo de datos de prueba
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
