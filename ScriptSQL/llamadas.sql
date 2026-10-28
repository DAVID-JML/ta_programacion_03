CALL asignar_cliente(
    '12345678',
    'juan@gmail.com',
    '987654321'
);
CALL asignar_veterinario(
    '23456781',
    'CMP-12345',
    'Odontología'
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
    'Lunes',
    '08:00:00',
    '12:00:00'
);
SELECT * FROM VETERINARIO;
SELECT * FROM HORARIO_ATENCION;

CALL vincular_mascota(
    '12345678',
    'Leia',
    '2020-05-10',
    'Perro',
    'Labrador',
    'Hembra'
);
SELECT * FROM MASCOTA;
CALL registrar_cita(
    '12345678',
    'Leia',
    '2026-09-20',
    '10:00:00',
    'Pendiente',
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