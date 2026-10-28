/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.6-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: database-mysql-prog3.comqaoeqw53k.us-east-1.rds.amazonaws.com    Database: prog3
-- ------------------------------------------------------
-- Server version	8.4.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `ADMINISTRADOR`
--

DROP TABLE IF EXISTS `ADMINISTRADOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADMINISTRADOR` (
  `id_usuario` int NOT NULL,
  `cargo` varchar(100) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `fk_usuario_administrador` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMINISTRADOR`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `ADMINISTRADOR` WRITE;
/*!40000 ALTER TABLE `ADMINISTRADOR` DISABLE KEYS */;
/*!40000 ALTER TABLE `ADMINISTRADOR` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `ATENCION_MEDICA`
--

DROP TABLE IF EXISTS `ATENCION_MEDICA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATENCION_MEDICA` (
  `id_atencion` int NOT NULL AUTO_INCREMENT,
  `observaciones` varchar(255) DEFAULT NULL,
  `peso_actual` double DEFAULT NULL,
  `alergias_identificadas` varchar(255) DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  `id_mascota` int NOT NULL,
  `id_cita` int NOT NULL,
  PRIMARY KEY (`id_atencion`),
  KEY `fk_mascota_atencion` (`id_mascota`),
  KEY `fk_atencion_cita` (`id_cita`),
  CONSTRAINT `fk_atencion_cita` FOREIGN KEY (`id_cita`) REFERENCES `CITA` (`id_cita`),
  CONSTRAINT `fk_mascota_atencion` FOREIGN KEY (`id_mascota`) REFERENCES `MASCOTA` (`id_mascota`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ATENCION_MEDICA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `ATENCION_MEDICA` WRITE;
/*!40000 ALTER TABLE `ATENCION_MEDICA` DISABLE KEYS */;
/*!40000 ALTER TABLE `ATENCION_MEDICA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `CIRUGIA`
--

DROP TABLE IF EXISTS `CIRUGIA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CIRUGIA` (
  `id_atencion` int NOT NULL,
  `procedimiento` varchar(500) NOT NULL,
  `indicaciones_post_operatorias` varchar(500) NOT NULL,
  PRIMARY KEY (`id_atencion`),
  CONSTRAINT `fk_cirugia_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `ATENCION_MEDICA` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CIRUGIA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `CIRUGIA` WRITE;
/*!40000 ALTER TABLE `CIRUGIA` DISABLE KEYS */;
/*!40000 ALTER TABLE `CIRUGIA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `CITA`
--

DROP TABLE IF EXISTS `CITA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CITA` (
  `id_cita` int NOT NULL AUTO_INCREMENT,
  `id_mascota` int NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `estado` varchar(150) NOT NULL,
  `id_usuario` int NOT NULL,
  `id_recepcionista` int DEFAULT NULL,
  `id_veterinario` int DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  KEY `fk_cliente_cita` (`id_usuario`),
  KEY `fk_recepcionista_cita` (`id_recepcionista`),
  CONSTRAINT `fk_cliente_cita` FOREIGN KEY (`id_usuario`) REFERENCES `CLIENTE` (`id_usuario`),
  CONSTRAINT `fk_mascota_cita` FOREIGN KEY (`id_mascota`) REFERENCES `MASCOTA` (`id_mascota`),
  CONSTRAINT `fk_recepcionista_cita` FOREIGN KEY (`id_recepcionista`) REFERENCES `RECEPCIONISTA` (`id_usuario`),
  CONSTRAINT `fk_veterinario_cita` FOREIGN KEY (`id_veterinario`) REFERENCES `VETERINARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CITA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `CITA` WRITE;
/*!40000 ALTER TABLE `CITA` DISABLE KEYS */;
/*!40000 ALTER TABLE `CITA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `CLIENTE`
--

DROP TABLE IF EXISTS `CLIENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENTE` (
  `id_usuario` int NOT NULL,
  `correo` varchar(200) NOT NULL,
  `telefono` varchar(100) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`),
  UNIQUE KEY `telefono` (`telefono`),
  CONSTRAINT `fk_usuario_cliente` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENTE`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `CLIENTE` WRITE;
/*!40000 ALTER TABLE `CLIENTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENTE` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `CONSULTA`
--

DROP TABLE IF EXISTS `CONSULTA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CONSULTA` (
  `id_atencion` int NOT NULL,
  `motivoConsulta` varchar(400) NOT NULL,
  `diagnostico` varchar(400) NOT NULL,
  `tratamiento` varchar(400) NOT NULL,
  PRIMARY KEY (`id_atencion`),
  CONSTRAINT `fk_consulta_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `ATENCION_MEDICA` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONSULTA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `CONSULTA` WRITE;
/*!40000 ALTER TABLE `CONSULTA` DISABLE KEYS */;
/*!40000 ALTER TABLE `CONSULTA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `CONTROL`
--

DROP TABLE IF EXISTS `CONTROL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CONTROL` (
  `id_atencion` int NOT NULL,
  `evolucion` varchar(500) NOT NULL,
  `indicaciones` varchar(500) NOT NULL,
  PRIMARY KEY (`id_atencion`),
  CONSTRAINT `fk_control_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `ATENCION_MEDICA` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTROL`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `CONTROL` WRITE;
/*!40000 ALTER TABLE `CONTROL` DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTROL` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `DETALLE_RECETA`
--

DROP TABLE IF EXISTS `DETALLE_RECETA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `DETALLE_RECETA` (
  `id_detalle_receta` int NOT NULL AUTO_INCREMENT,
  `id_receta` int NOT NULL,
  `id_medicamento` int NOT NULL,
  `dosis` varchar(200) NOT NULL,
  `frecuencia` varchar(200) NOT NULL,
  `duracion` varchar(200) NOT NULL,
  `montoTotal` double DEFAULT NULL,
  PRIMARY KEY (`id_detalle_receta`),
  KEY `fk_detalleR_receta` (`id_receta`),
  KEY `fk_detalle_receta_medicamento` (`id_medicamento`),
  CONSTRAINT `fk_detalleR_receta` FOREIGN KEY (`id_receta`) REFERENCES `RECETA` (`id_receta`),
  CONSTRAINT `fk_detalle_receta_medicamento` FOREIGN KEY (`id_medicamento`) REFERENCES `MEDICAMENTO` (`id_medicamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DETALLE_RECETA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `DETALLE_RECETA` WRITE;
/*!40000 ALTER TABLE `DETALLE_RECETA` DISABLE KEYS */;
/*!40000 ALTER TABLE `DETALLE_RECETA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `HORARIO_ATENCION`
--

DROP TABLE IF EXISTS `HORARIO_ATENCION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `HORARIO_ATENCION` (
  `id_horario` int NOT NULL AUTO_INCREMENT,
  `id_veterinario` int NOT NULL,
  `dia` varchar(100) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `fk_horario_veterinario` (`id_veterinario`),
  CONSTRAINT `fk_horario_veterinario` FOREIGN KEY (`id_veterinario`) REFERENCES `VETERINARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HORARIO_ATENCION`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `HORARIO_ATENCION` WRITE;
/*!40000 ALTER TABLE `HORARIO_ATENCION` DISABLE KEYS */;
/*!40000 ALTER TABLE `HORARIO_ATENCION` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `MASCOTA`
--

DROP TABLE IF EXISTS `MASCOTA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `MASCOTA` (
  `id_mascota` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `fechaNacimiento` date NOT NULL,
  `especie` varchar(150) NOT NULL,
  `raza` varchar(150) NOT NULL,
  `sexo` varchar(150) NOT NULL,
  `estado` varchar(150) NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_mascota`),
  KEY `fk_mascota_cliente` (`id_usuario`),
  CONSTRAINT `fk_mascota_cliente` FOREIGN KEY (`id_usuario`) REFERENCES `CLIENTE` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MASCOTA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `MASCOTA` WRITE;
/*!40000 ALTER TABLE `MASCOTA` DISABLE KEYS */;
/*!40000 ALTER TABLE `MASCOTA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `MEDICAMENTO`
--

DROP TABLE IF EXISTS `MEDICAMENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEDICAMENTO` (
  `id_medicamento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `monto` double NOT NULL,
  PRIMARY KEY (`id_medicamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEDICAMENTO`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `MEDICAMENTO` WRITE;
/*!40000 ALTER TABLE `MEDICAMENTO` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEDICAMENTO` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `RECEPCIONISTA`
--

DROP TABLE IF EXISTS `RECEPCIONISTA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECEPCIONISTA` (
  `id_usuario` int NOT NULL,
  `turno` varchar(150) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `fk_usuario_recepcionista` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RECEPCIONISTA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `RECEPCIONISTA` WRITE;
/*!40000 ALTER TABLE `RECEPCIONISTA` DISABLE KEYS */;
/*!40000 ALTER TABLE `RECEPCIONISTA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `RECETA`
--

DROP TABLE IF EXISTS `RECETA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECETA` (
  `id_receta` int NOT NULL AUTO_INCREMENT,
  `id_atencion` int NOT NULL,
  `fecha` date NOT NULL,
  `indicaciones` varchar(600) NOT NULL,
  PRIMARY KEY (`id_receta`),
  UNIQUE KEY `id_atencion` (`id_atencion`),
  CONSTRAINT `fk_receta_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `ATENCION_MEDICA` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RECETA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `RECETA` WRITE;
/*!40000 ALTER TABLE `RECETA` DISABLE KEYS */;
/*!40000 ALTER TABLE `RECETA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `USUARIO`
--

DROP TABLE IF EXISTS `USUARIO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `USUARIO` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(15) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `apellido` varchar(150) NOT NULL,
  `nombre_usuario` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USUARIO`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `USUARIO` WRITE;
/*!40000 ALTER TABLE `USUARIO` DISABLE KEYS */;
/*!40000 ALTER TABLE `USUARIO` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `VACUNA`
--

DROP TABLE IF EXISTS `VACUNA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `VACUNA` (
  `id_vacuna` int NOT NULL AUTO_INCREMENT,
  `id_vacunacion` int DEFAULT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` varchar(500) NOT NULL,
  PRIMARY KEY (`id_vacuna`),
  KEY `fk_vacuna_vacunacion` (`id_vacunacion`),
  CONSTRAINT `fk_vacuna_vacunacion` FOREIGN KEY (`id_vacunacion`) REFERENCES `VACUNACION` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VACUNA`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `VACUNA` WRITE;
/*!40000 ALTER TABLE `VACUNA` DISABLE KEYS */;
/*!40000 ALTER TABLE `VACUNA` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `VACUNACION`
--

DROP TABLE IF EXISTS `VACUNACION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `VACUNACION` (
  `id_atencion` int NOT NULL,
  `fechaAplicacion` date NOT NULL,
  `fechaProximaDosis` date NOT NULL,
  `dosis` varchar(200) NOT NULL,
  PRIMARY KEY (`id_atencion`),
  CONSTRAINT `fk_vacunacion_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `ATENCION_MEDICA` (`id_atencion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VACUNACION`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `VACUNACION` WRITE;
/*!40000 ALTER TABLE `VACUNACION` DISABLE KEYS */;
/*!40000 ALTER TABLE `VACUNACION` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `VETERINARIO`
--

DROP TABLE IF EXISTS `VETERINARIO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `VETERINARIO` (
  `id_usuario` int NOT NULL,
  `num_colegiatura` varchar(200) NOT NULL,
  `especialidad` varchar(255) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `num_colegiatura` (`num_colegiatura`),
  CONSTRAINT `fk_usuario_veterinario` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VETERINARIO`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `VETERINARIO` WRITE;
/*!40000 ALTER TABLE `VETERINARIO` DISABLE KEYS */;
/*!40000 ALTER TABLE `VETERINARIO` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Dumping routines for database 'prog3'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-09-14 20:29:50
