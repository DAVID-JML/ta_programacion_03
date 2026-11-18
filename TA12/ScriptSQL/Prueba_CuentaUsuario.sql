 
USE mascovetd;
 
 
CALL VERIFICAR_CUENTA_USUARIO('ana.rojas', 'Luna#84Pez27');
 
CALL VERIFICAR_CUENTA_USUARIO('bruno.mendoza', 'Nube@39Sol62');
 
CALL VERIFICAR_CUENTA_USUARIO('carla.paredes', 'Roca!75Mar18');
 
CALL VERIFICAR_CUENTA_USUARIO('luis.salazar', 'Faro#26Rio94');
 
CALL VERIFICAR_CUENTA_USUARIO('elena.campos', 'Hoja@61Paz38');
 
CALL VERIFICAR_CUENTA_USUARIO('mario.benavides', 'Tigre!48Luz23');
 
CALL VERIFICAR_CUENTA_USUARIO('diana.huaman', 'Brisa#93Oro51');
 
CALL VERIFICAR_CUENTA_USUARIO('jorge.delgado', 'Cielo@57Flor86');
 
CALL VERIFICAR_CUENTA_USUARIO('sofia.leon', 'Bosque!72Miel40');
 
CALL VERIFICAR_CUENTA_USUARIO('sofia.leon', 'incorrecta');
 
SELECT COUNT(*) AS usuarios_con_contrasena_legada FROM USUARIO WHERE contrasena <> '';
SELECT cu.username, cu.activo, LENGTH(cu.password) AS longitud_md5 FROM CUENTA_USUARIO cu ORDER BY cu.id_cuenta_usuario;
