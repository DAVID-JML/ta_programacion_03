 
 
 
USE mascovetd;
START TRANSACTION;

 
UPDATE USUARIO
SET nombre_usuario = CASE nombre_usuario
        WHEN 'prueba_cliente_ana' THEN 'ana.rojas'
        WHEN 'prueba_cliente_bruno' THEN 'bruno.mendoza'
        WHEN 'prueba_cliente_carla' THEN 'carla.paredes'
        WHEN 'prueba_veterinario_luis' THEN 'luis.salazar'
        WHEN 'prueba_veterinario_elena' THEN 'elena.campos'
        WHEN 'prueba_veterinario_mario' THEN 'mario.benavides'
        WHEN 'prueba_recepcionista_diana' THEN 'diana.huaman'
        WHEN 'prueba_recepcionista_jorge' THEN 'jorge.delgado'
        WHEN 'prueba_administrador_sofia' THEN 'sofia.leon'
    ELSE nombre_usuario END
WHERE nombre_usuario IN ('prueba_cliente_ana','prueba_cliente_bruno','prueba_cliente_carla','prueba_veterinario_luis','prueba_veterinario_elena','prueba_veterinario_mario','prueba_recepcionista_diana','prueba_recepcionista_jorge','prueba_administrador_sofia');

 
 
UPDATE CUENTA_USUARIO cu
INNER JOIN USUARIO u ON u.id_usuario = cu.fid_usuario
SET cu.username = u.nombre_usuario,
    cu.password = MD5(CASE u.nombre_usuario
        WHEN 'ana.rojas' THEN 'Luna#84Pez27'
        WHEN 'bruno.mendoza' THEN 'Nube@39Sol62'
        WHEN 'carla.paredes' THEN 'Roca!75Mar18'
        WHEN 'luis.salazar' THEN 'Faro#26Rio94'
        WHEN 'elena.campos' THEN 'Hoja@61Paz38'
        WHEN 'mario.benavides' THEN 'Tigre!48Luz23'
        WHEN 'diana.huaman' THEN 'Brisa#93Oro51'
        WHEN 'jorge.delgado' THEN 'Cielo@57Flor86'
        WHEN 'sofia.leon' THEN 'Bosque!72Miel40'
        ELSE '' END)
WHERE cu.username IN ('prueba_cliente_ana','prueba_cliente_bruno','prueba_cliente_carla','prueba_veterinario_luis','prueba_veterinario_elena','prueba_veterinario_mario','prueba_recepcionista_diana','prueba_recepcionista_jorge','prueba_administrador_sofia')
  AND u.nombre_usuario IN ('ana.rojas','bruno.mendoza','carla.paredes','luis.salazar','elena.campos','mario.benavides','diana.huaman','jorge.delgado','sofia.leon');

 
UPDATE CLIENTE c JOIN USUARIO u ON u.id_usuario=c.id_usuario
SET c.correo = CONCAT(u.nombre_usuario,'@mascovet.example')
WHERE u.nombre_usuario IN ('ana.rojas','bruno.mendoza','carla.paredes');
UPDATE VETERINARIO v JOIN USUARIO u ON u.id_usuario=v.id_usuario
SET v.correo = CONCAT(u.nombre_usuario,'@mascovet.example')
WHERE u.nombre_usuario IN ('luis.salazar','elena.campos','mario.benavides');
UPDATE RECEPCIONISTA r JOIN USUARIO u ON u.id_usuario=r.id_usuario
SET r.correo = CONCAT(u.nombre_usuario,'@mascovet.example')
WHERE u.nombre_usuario IN ('diana.huaman','jorge.delgado');

COMMIT;
 
SELECT u.nombre, u.apellido_paterno, cu.username, LENGTH(cu.password) AS longitud_hash
FROM CUENTA_USUARIO cu JOIN USUARIO u ON u.id_usuario=cu.fid_usuario
WHERE cu.username IN ('ana.rojas','bruno.mendoza','carla.paredes','luis.salazar','elena.campos','mario.benavides','diana.huaman','jorge.delgado','sofia.leon');
