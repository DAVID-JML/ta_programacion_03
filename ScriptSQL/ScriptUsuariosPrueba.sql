-- Usuarios de prueba para Mascovet.
-- Ejecutar después de ScriptSQL_Estructura.sql y antes de ScriptDatosPrueba.sql.
-- Contraseña de desarrollo de las nueve cuentas: demo1234.
-- Formato compatible con ContrasenaHash.java:
-- PBKDF2-HMAC-SHA256, 600000 iteraciones, salt aleatorio de 16 bytes, 256 bits.

USE mascovetd;
START TRANSACTION;

INSERT INTO USUARIO
    (dni, nombre, apellido_paterno, apellido_materno, nombre_usuario, contrasena, activo)
SELECT s.dni, s.nombre, s.apellido_paterno, s.apellido_materno,
       s.nombre_usuario, s.contrasena, s.activo
FROM (
    SELECT '99001001' AS dni, 'Ana' AS nombre, 'Rojas' AS apellido_paterno, 'Castillo' AS apellido_materno, 'prueba_cliente_ana' AS nombre_usuario, 'pbkdf2-sha256-v1$i5RcL/4Llefl3xAcsDSfVw==$B4mlRZZmF/vnVHbJMnNLXAXB+fM/817pyRjzvMxU22c=' AS contrasena, 1 AS activo
    UNION ALL
    SELECT '99001002', 'Bruno', 'Mendoza', 'Reyes', 'prueba_cliente_bruno', 'pbkdf2-sha256-v1$junyIigOzQ2OVA36r/LQNw==$tPuUyXiUlDL7DZVucQOSrUTq9zxmyIULjllmwQPEDec=', 1
    UNION ALL
    SELECT '99001003', 'Carla', 'Paredes', 'Vega', 'prueba_cliente_carla', 'pbkdf2-sha256-v1$IUmL9B79IU+EsnrCOWT6Jw==$u44i6uZvhbnsScHvq6iUwtzNwMSIke7PExjAUuOB3lw=', 1
    UNION ALL
    SELECT '99001004', 'Luis', 'Salazar', 'Torres', 'prueba_veterinario_luis', 'pbkdf2-sha256-v1$f6YCbJnzah+P0ZOcliWETg==$5Cj4lSv9krLzFkzpbE0m3XLtnlkNg96pYG2pGBYF/HE=', 1
    UNION ALL
    SELECT '99001005', 'Elena', 'Campos', 'Flores', 'prueba_veterinario_elena', 'pbkdf2-sha256-v1$0BshAUg9yYzrmDOvp1ItWQ==$b3AL5NjRLRr9a7MZCbknPA3IOk+X2fd4BSjJe/TTTeQ=', 1
    UNION ALL
    SELECT '99001006', 'Mario', 'Benavides', 'Ramos', 'prueba_veterinario_mario', 'pbkdf2-sha256-v1$IPHfCqucdW9fc+iA6LGR8g==$fkMaalUZSX0CG0D3Q8nQelLyU7wuQByNAk4lmWx6suk=', 1
    UNION ALL
    SELECT '99001007', 'Diana', 'Huaman', 'Luna', 'prueba_recepcionista_diana', 'pbkdf2-sha256-v1$sU/fJTxlqyH32XS+pVCMRw==$7nR4XNveeR/MoWV4G1cXrPkZQqPGEf1N/vJjnRX8Pow=', 1
    UNION ALL
    SELECT '99001008', 'Jorge', 'Delgado', 'Paz', 'prueba_recepcionista_jorge', 'pbkdf2-sha256-v1$iCzWEHJrdXNz4/JTyU8F0w==$O1hlT6YUA8hBnHSk0p7jwAn7v0er8TXOPXuEVZ0hw3U=', 1
    UNION ALL
    SELECT '99001009', 'Sofia', 'Leon', 'Navarro', 'prueba_administrador_sofia', 'pbkdf2-sha256-v1$RwqcZtBBHn4Vhcvy7jk6nA==$7TjGHQ689FUQSDB635iY5zjseM5Ib8iIV1LJBv99eYo=', 1
) AS s
WHERE NOT EXISTS (
    SELECT 1 FROM USUARIO u
    WHERE u.nombre_usuario = s.nombre_usuario OR u.dni = s.dni
);

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

COMMIT;
