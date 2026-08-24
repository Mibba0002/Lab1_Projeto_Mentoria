-- =============================================================
-- MIRACLE MENTORIAS - SCRIPT UNICO DO BANCO DE DADOS
-- =============================================================

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE;
SET SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `mydb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE `mydb`;

-- -------------------------------------------------------------
-- USUARIOS E ADMINISTRACAO
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `admin` (
  `id_admin` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(100) NOT NULL,
  `status` VARCHAR(30) NOT NULL DEFAULT 'Ativo',
  `nivel_acesso` VARCHAR(30) NOT NULL DEFAULT 'Admin',
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `uk_admin_email` (`email`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Mentor` (
  `cpf_mentor` VARCHAR(14) NOT NULL,
  `nome` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(100) NOT NULL,
  `telefone` VARCHAR(20) NOT NULL,
  `mini_biografia` TEXT NOT NULL,
  `disponibilidade` VARCHAR(100) NOT NULL,
  `formato_mentoria` VARCHAR(50) NOT NULL,
  `link_portifolio` VARCHAR(200) NOT NULL,
  `redes_profissionais` VARCHAR(200) NOT NULL,
  `status` VARCHAR(30) NOT NULL DEFAULT 'Ativo',
  `cidade` VARCHAR(100) NOT NULL,
  `estado` CHAR(2) NOT NULL,
  `verificado` TINYINT NOT NULL DEFAULT 0,
  `data_verificacao` DATETIME NULL,
  `id_admin_verificador` INT NULL,
  PRIMARY KEY (`cpf_mentor`),
  UNIQUE KEY `uk_mentor_email` (`email`),
  KEY `idx_mentor_admin_verificador` (`id_admin_verificador`),
  CONSTRAINT `fk_mentor_admin_verificador`
    FOREIGN KEY (`id_admin_verificador`) REFERENCES `admin` (`id_admin`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Mentorado` (
  `cpf_mentorado` VARCHAR(14) NOT NULL,
  `nome` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(100) NOT NULL,
  `telefone` VARCHAR(20) NOT NULL,
  `formacao` VARCHAR(100) NOT NULL,
  `objetivos_profissionais` TEXT NOT NULL,
  `principais_duvidas` TEXT NOT NULL,
  `expectativas` TEXT NOT NULL,
  `cidade` VARCHAR(100) NOT NULL,
  `estado` CHAR(2) NOT NULL,
  `status` VARCHAR(30) NOT NULL DEFAULT 'Ativo',
  PRIMARY KEY (`cpf_mentorado`),
  UNIQUE KEY `uk_mentorado_email` (`email`)
) ENGINE = InnoDB;

-- O bloqueio de Mentor e Mentorado e feito pelo campo status.
-- O login da aplicacao somente autentica usuarios com status = 'Ativo'.

-- -------------------------------------------------------------
-- AREAS, ESPECIALIZACOES E INTERESSES
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `area_atuacao` (
  `id_area_atuacao` INT NOT NULL AUTO_INCREMENT,
  `nome_area` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_area_atuacao`),
  UNIQUE KEY `uk_area_nome` (`nome_area`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Especializacao_em` (
  `id_especializacao` INT NOT NULL AUTO_INCREMENT,
  `id_area_atuacao` INT NOT NULL,
  `cpf_mentor` VARCHAR(14) NOT NULL,
  `especializacao` VARCHAR(100) NOT NULL,
  `tempo_experiencia` INT NOT NULL,
  PRIMARY KEY (`id_especializacao`),
  KEY `idx_especializacao_area` (`id_area_atuacao`),
  KEY `idx_especializacao_mentor` (`cpf_mentor`),
  CONSTRAINT `fk_especializacao_area`
    FOREIGN KEY (`id_area_atuacao`) REFERENCES `area_atuacao` (`id_area_atuacao`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_especializacao_mentor`
    FOREIGN KEY (`cpf_mentor`) REFERENCES `Mentor` (`cpf_mentor`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `interesse_em` (
  `id_area_atuacao` INT NOT NULL,
  `cpf_mentorado` VARCHAR(14) NOT NULL,
  `area_interesse` VARCHAR(100) NOT NULL,
  `nivel_experiencia` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_area_atuacao`, `cpf_mentorado`),
  KEY `idx_interesse_mentorado` (`cpf_mentorado`),
  CONSTRAINT `fk_interesse_area`
    FOREIGN KEY (`id_area_atuacao`) REFERENCES `area_atuacao` (`id_area_atuacao`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_interesse_mentorado`
    FOREIGN KEY (`cpf_mentorado`) REFERENCES `Mentorado` (`cpf_mentorado`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- MENTORIAS, ENCONTROS, DIARIO E AVALIACOES
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Mentoria` (
  `id_mentoria` INT NOT NULL AUTO_INCREMENT,
  `cpf_mentor` VARCHAR(14) NOT NULL,
  `cpf_mentorado` VARCHAR(14) NOT NULL,
  `id_especializacao` INT NOT NULL,
  `status` VARCHAR(30) NOT NULL DEFAULT 'Pendente',
  `data_inicio` DATE NOT NULL,
  `data_fim` DATE NULL,
  `objetivos_definidos` TEXT NOT NULL,
  `depoimentos` TEXT NULL,
  PRIMARY KEY (`id_mentoria`),
  KEY `idx_mentoria_mentor` (`cpf_mentor`),
  KEY `idx_mentoria_mentorado` (`cpf_mentorado`),
  KEY `idx_mentoria_especializacao` (`id_especializacao`),
  CONSTRAINT `fk_mentoria_mentor`
    FOREIGN KEY (`cpf_mentor`) REFERENCES `Mentor` (`cpf_mentor`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_mentoria_mentorado`
    FOREIGN KEY (`cpf_mentorado`) REFERENCES `Mentorado` (`cpf_mentorado`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_mentoria_especializacao`
    FOREIGN KEY (`id_especializacao`) REFERENCES `Especializacao_em` (`id_especializacao`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Encontro` (
  `id_encontro` INT NOT NULL AUTO_INCREMENT,
  `id_mentoria` INT NOT NULL,
  `data` DATE NOT NULL,
  `horario` TIME NOT NULL,
  `tipo_encontro` VARCHAR(20) NOT NULL,
  `descricao` TEXT NOT NULL,
  `link_reuniao` VARCHAR(200) NULL,
  `status` VARCHAR(30) NOT NULL DEFAULT 'Agendado',
  `motivo_nao_realizacao` TEXT NULL,
  `local_encontro` VARCHAR(200) NULL,
  PRIMARY KEY (`id_encontro`),
  KEY `idx_encontro_mentoria` (`id_mentoria`),
  CONSTRAINT `fk_encontro_mentoria`
    FOREIGN KEY (`id_mentoria`) REFERENCES `Mentoria` (`id_mentoria`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Diario_bordo` (
  `id_diario` INT NOT NULL AUTO_INCREMENT,
  `id_mentoria` INT NOT NULL,
  `tipo_autor` VARCHAR(20) NOT NULL,
  `cpf_autor` VARCHAR(14) NOT NULL,
  `conteudo` TEXT NOT NULL,
  `data_registro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_diario`),
  KEY `idx_diario_mentoria` (`id_mentoria`),
  CONSTRAINT `fk_diario_mentoria`
    FOREIGN KEY (`id_mentoria`) REFERENCES `Mentoria` (`id_mentoria`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Avaliacao` (
  `id_avaliacao` INT NOT NULL AUTO_INCREMENT,
  `id_mentoria` INT NOT NULL,
  `nota` TINYINT NOT NULL,
  `comentario` TEXT NULL,
  `data_avaliacao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_avaliacao`),
  UNIQUE KEY `uk_avaliacao_mentoria` (`id_mentoria`),
  CONSTRAINT `fk_avaliacao_mentoria`
    FOREIGN KEY (`id_mentoria`) REFERENCES `Mentoria` (`id_mentoria`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_avaliacao_nota` CHECK (`nota` BETWEEN 1 AND 5)
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- COMPATIBILIDADE COM VERSOES ANTIGAS DO BANCO
-- Estes blocos nao apagam os dados existentes.
-- -------------------------------------------------------------

SET @sql = IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'Mentor'
     AND COLUMN_NAME = 'telefone') = 0,
  'ALTER TABLE `Mentor` ADD COLUMN `telefone` VARCHAR(20) NULL AFTER `senha`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'Mentorado'
     AND COLUMN_NAME = 'telefone') = 0,
  'ALTER TABLE `Mentorado` ADD COLUMN `telefone` VARCHAR(20) NULL AFTER `senha`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(
  (SELECT COUNT(*) FROM information_schema.COLUMNS
   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'Encontro'
     AND COLUMN_NAME = 'motivo_nao_realizacao') = 0,
  'ALTER TABLE `Encontro` ADD COLUMN `motivo_nao_realizacao` TEXT NULL AFTER `status`',
  'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- -------------------------------------------------------------
-- DADOS INICIAIS
-- -------------------------------------------------------------

INSERT IGNORE INTO `area_atuacao` (`nome_area`) VALUES
  ('Eventos Corporativos'),
  ('Eventos Sociais'),
  ('Eventos Culturais'),
  ('Eventos Esportivos'),
  ('Marketing'),
  ('Produção'),
  ('Cerimonial'),
  ('Logística');

INSERT INTO `admin` (`nome`, `email`, `senha`, `status`, `nivel_acesso`)
SELECT 'Administrador', 'admin@miracle.com', 'admin123', 'Ativo', 'SUPER_ADMIN'
WHERE NOT EXISTS (
  SELECT 1 FROM `admin` WHERE `email` = 'admin@miracle.com'
);

UPDATE `admin`
SET `nivel_acesso` = 'SUPER_ADMIN'
WHERE `email` = 'admin@miracle.com';

SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

SELECT 'Banco MiracleMentorias criado/atualizado com sucesso.' AS resultado;
