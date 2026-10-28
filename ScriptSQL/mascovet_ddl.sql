DROP DATABASE IF EXISTS mascovet;
CREATE DATABASE mascovet
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE mascovet;

CREATE TABLE USUARIO (
    id_usuario      INT NOT NULL AUTO_INCREMENT,
    dni             VARCHAR(9)  NOT NULL,
    nombre          VARCHAR(30) NOT NULL,
    apellido        VARCHAR(30) NOT NULL,
    nombre_usuario  VARCHAR(50) NOT NULL,
    contrasena      VARCHAR(255) NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,

    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_dni UNIQUE (dni),
    CONSTRAINT uq_usuario_nombre_usuario UNIQUE (nombre_usuario)
);

CREATE TABLE CLIENTE (
    id_usuario  INT NOT NULL,
    correo      VARCHAR(200) NOT NULL,
    telefono    VARCHAR(15) NOT NULL,

    CONSTRAINT pk_cliente PRIMARY KEY (id_usuario),
    CONSTRAINT uq_cliente_correo UNIQUE (correo),
    CONSTRAINT uq_cliente_telefono UNIQUE (telefono),
    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE VETERINARIO (
    id_usuario       INT NOT NULL,
    num_colegiatura  VARCHAR(20) NOT NULL,
    especialidad     VARCHAR(20) NOT NULL,

    CONSTRAINT pk_veterinario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_veterinario_num_colegiatura UNIQUE (num_colegiatura),
    CONSTRAINT fk_veterinario_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE RECEPCIONISTA (
    id_usuario  INT NOT NULL,
    turno       VARCHAR(50) NOT NULL,

    CONSTRAINT pk_recepcionista PRIMARY KEY (id_usuario),
    CONSTRAINT fk_recepcionista_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE ADMINISTRADOR (
    id_usuario  INT NOT NULL,
    cargo       VARCHAR(50) NOT NULL,

    CONSTRAINT pk_administrador PRIMARY KEY (id_usuario),
    CONSTRAINT fk_administrador_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE MASCOTA (
    id_mascota       INT NOT NULL AUTO_INCREMENT,
    id_usuario       INT NOT NULL,
    nombre           VARCHAR(30) NOT NULL,
    fechaNacimiento  DATE NOT NULL,
    especie          VARCHAR(10) NOT NULL,
    raza             VARCHAR(60) NOT NULL,
    sexo             VARCHAR(10) NOT NULL,
    activo           TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT pk_mascota PRIMARY KEY (id_mascota),
    KEY idx_mascota_cliente (id_usuario),
    CONSTRAINT fk_mascota_cliente
        FOREIGN KEY (id_usuario)
        REFERENCES CLIENTE(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE HORARIO_ATENCION (
    id_horario      INT NOT NULL AUTO_INCREMENT,
    id_veterinario  INT NOT NULL,
    dia             VARCHAR(10) NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,

    CONSTRAINT pk_horario_atencion PRIMARY KEY (id_horario),
    KEY idx_horario_veterinario (id_veterinario),
    CONSTRAINT fk_horario_veterinario
        FOREIGN KEY (id_veterinario)
        REFERENCES VETERINARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE CITA (
    id_cita           INT NOT NULL AUTO_INCREMENT,
    id_mascota        INT NOT NULL,
    fecha             DATE NOT NULL,
    hora              TIME NOT NULL,
    estado            VARCHAR(20) NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    motivoCancelacion VARCHAR(100) DEFAULT NULL,
    id_recepcionista  INT DEFAULT NULL,
    id_veterinario    INT DEFAULT NULL,

    CONSTRAINT pk_cita PRIMARY KEY (id_cita),

    KEY idx_cita_mascota (id_mascota),
    KEY idx_cita_recepcionista (id_recepcionista),
    KEY idx_cita_veterinario (id_veterinario),

    CONSTRAINT fk_cita_mascota
        FOREIGN KEY (id_mascota)
        REFERENCES MASCOTA(id_mascota)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_cita_recepcionista
        FOREIGN KEY (id_recepcionista)
        REFERENCES RECEPCIONISTA(id_usuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_cita_veterinario
        FOREIGN KEY (id_veterinario)
        REFERENCES VETERINARIO(id_usuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE ATENCION_MEDICA (
    id_atencion    INT NOT NULL AUTO_INCREMENT,
    observaciones  VARCHAR(255) DEFAULT NULL,
    hora_inicio    TIME DEFAULT NULL,
    hora_fin       TIME DEFAULT NULL,
    id_cita        INT NOT NULL,
    peso_actual    DOUBLE NOT NULL DEFAULT 0,
    alergias       VARCHAR(255) DEFAULT NULL,

    CONSTRAINT pk_atencion_medica PRIMARY KEY (id_atencion),
    KEY idx_atencion_cita (id_cita),

    CONSTRAINT fk_atencion_cita
        FOREIGN KEY (id_cita)
        REFERENCES CITA(id_cita)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE CONSULTA (
    id_atencion     INT NOT NULL,
    motivoConsulta  VARCHAR(150) NOT NULL,
    diagnostico     VARCHAR(150) NOT NULL,
    tratamiento     VARCHAR(255) NOT NULL,

    CONSTRAINT pk_consulta PRIMARY KEY (id_atencion),
    CONSTRAINT fk_consulta_atencion
        FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE CONTROL (
    id_atencion  INT NOT NULL,
    evolucion    VARCHAR(150) NOT NULL,
    indicaciones VARCHAR(255) NOT NULL,

    CONSTRAINT pk_control PRIMARY KEY (id_atencion),
    CONSTRAINT fk_control_atencion
        FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE CIRUGIA (
    id_atencion                    INT NOT NULL,
    procedimiento                  VARCHAR(150) NOT NULL,
    indicaciones_post_operatorias  VARCHAR(255) NOT NULL,

    CONSTRAINT pk_cirugia PRIMARY KEY (id_atencion),
    CONSTRAINT fk_cirugia_atencion
        FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE VACUNACION (
    id_atencion        INT NOT NULL,
    fechaAplicacion    DATE NOT NULL,
    fechaProximaDosis  DATE NOT NULL,
    dosis              VARCHAR(50) NOT NULL,

    CONSTRAINT pk_vacunacion PRIMARY KEY (id_atencion),
    CONSTRAINT fk_vacunacion_atencion
        FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
	
CREATE TABLE VACUNA (
    id_vacuna      INT NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(50) NOT NULL,
    descripcion    VARCHAR(255) NOT NULL,

    CONSTRAINT pk_vacuna PRIMARY KEY (id_vacuna)
);

CREATE TABLE RECETA (
    id_receta     INT NOT NULL AUTO_INCREMENT,
    id_atencion   INT NOT NULL,
    fecha         DATE NOT NULL,
    indicaciones  VARCHAR(255) NOT NULL,

    CONSTRAINT pk_receta PRIMARY KEY (id_receta),
    CONSTRAINT uq_receta_atencion UNIQUE (id_atencion),
    CONSTRAINT fk_receta_atencion
        FOREIGN KEY (id_atencion)
        REFERENCES ATENCION_MEDICA(id_atencion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE MEDICAMENTO (
    id_medicamento  INT NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(50) NOT NULL,
    descripcion     VARCHAR(200) NOT NULL,
    monto           DOUBLE NOT NULL,
    fechaVencimiento   DATE NULL,

    CONSTRAINT pk_medicamento PRIMARY KEY (id_medicamento)
);

CREATE TABLE DETALLE_RECETA (
    id_detalle_receta  INT NOT NULL AUTO_INCREMENT,
    id_receta          INT NOT NULL,
    id_medicamento     INT NOT NULL,
    dosis              VARCHAR(50) NOT NULL,
    frecuencia         VARCHAR(50) NOT NULL,
    duracion           VARCHAR(50) NOT NULL,
    montoTotal         DOUBLE DEFAULT NULL,

    CONSTRAINT pk_detalle_receta PRIMARY KEY (id_detalle_receta),
    KEY idx_detalle_receta (id_receta),
    KEY idx_detalle_medicamento (id_medicamento),

    CONSTRAINT fk_detalle_receta
        FOREIGN KEY (id_receta)
        REFERENCES RECETA(id_receta)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES MEDICAMENTO(id_medicamento)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE VACUNACION_VACUNA (
    id_atencion INT NOT NULL,
    id_vacuna INT NOT NULL,
    PRIMARY KEY (id_atencion, id_vacuna),
    CONSTRAINT fk_vv_vacunacion FOREIGN KEY (id_atencion)
        REFERENCES VACUNACION (id_atencion)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_vv_vacuna FOREIGN KEY (id_vacuna)
        REFERENCES VACUNA (id_vacuna)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


