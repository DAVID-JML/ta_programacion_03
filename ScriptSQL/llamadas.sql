CALL asignar_cliente(
    '12345678',
    'juan@gmail.com',
    '987654321'
);
CALL asignar_veterinario(
    '23456781',
    'CMP-12345',
    'ODONTOLOGIA'
);
CALL asignar_recepcionista(
    '34567812',
    'Mañana'
);
CALL asignar_administrador(
    '45678123',
    'Gerente'
);

CALL asignar_horario_veterinario(
    '23456781',
    'LUNES',
    '08:00:00',
    '12:00:00'
);
SELECT * FROM VETERINARIO;
SELECT * FROM HORARIO_ATENCION;
SELECT * FROM CLIENTE;
CALL vincular_mascota(
    '12345678',
    'Leia',
    '2020-05-10',
    'PERRO',
    'Labrador',
    'HEMBRA'
);
SELECT * FROM MASCOTA;
CALL registrar_cita(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'RESERVADA',
    '23456781'
);
SELECT * FROM CITA;
CALL crear_atencion_medica(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'La mascota presenta gingivitis leve.',
    '10:05:00',
    '10:30:00'
);
SELECT * FROM ATENCION_MEDICA;
CALL cambiar_estado_mascota(
    '12345678',
    'Leia',
    1
);
SELECT * FROM MASCOTA;
CALL registrar_consulta(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'La mascota presenta gingivitis leve.',
    'Gingivitis leve',
    'Limpieza dental'
);
SELECT * FROM CONSULTA;
CALL registrar_cirugia(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'Limpieza dental',
    'control en 10 dias'
);
SELECT * FROM CIRUGIA;
CALL registrar_control(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'La evolución es favorable.',
    'Continuar con el tratamiento'
);
SELECT * FROM CONTROL;
CALL registrar_receta(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    '2026-09-20',
    'Seguir dosis indicada'
);
SELECT * FROM RECETA;
CALL registrar_vacunacion(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    '2026-09-20',
    '2027-09-20',
    '1 dosis'
);
SELECT * FROM VACUNACION;
