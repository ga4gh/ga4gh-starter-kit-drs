-- create schema
CREATE SCHEMA IF NOT EXISTS `starter_kit_db`;
USE `starter_kit_db`;

-- drs_object
CREATE TABLE IF NOT EXISTS `drs_object` (
  `id` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `mime_type` VARCHAR(255),
  `name` TEXT,
  `size` bigint unsigned DEFAULT NULL,
  `version` text,
  `is_bundle` tinyint DEFAULT 0,
  `created_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- file_access_object
CREATE TABLE IF NOT EXISTS `file_access_object` (
  `id` int NOT NULL AUTO_INCREMENT,
  `drs_object_id` VARCHAR(255) NOT NULL,
  `path` TEXT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `file_drs_fk`
    FOREIGN KEY (`drs_object_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- file_access_object
CREATE TABLE IF NOT EXISTS `aws_s3_access_object` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `drs_object_id` VARCHAR(255) NOT NULL,
  `region` VARCHAR(255) NULL,
  `bucket` VARCHAR(255) NULL,
  `key` TEXT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `aws_drs_fk`
    FOREIGN KEY (`drs_object_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- drs_object_alias
CREATE TABLE IF NOT EXISTS `drs_object_alias` (
  `drs_object_id` VARCHAR(255) NOT NULL,
  `alias` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`drs_object_id`, `alias`),
  CONSTRAINT `alias_drs_fk`
    FOREIGN KEY (`drs_object_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- drs_object_checksum
CREATE TABLE IF NOT EXISTS `drs_object_checksum` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `drs_object_id` VARCHAR(255) NOT NULL,
  `checksum` TINYTEXT NULL,
  `type` ENUM('md5', 'etag', 'crc32c', 'trunc512', 'sha1', 'sha256') NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `cs_drs_fk`
    FOREIGN KEY (`drs_object_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- drs_object_bundle
CREATE TABLE IF NOT EXISTS `drs_object_bundle` (
  `parent_id` VARCHAR(255) NOT NULL,
  `child_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`parent_id`, `child_id`),
  CONSTRAINT `parent_drs_fk`
    FOREIGN KEY (`parent_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `child_drs_fk`
    FOREIGN KEY (`child_id`)
    REFERENCES `starter_kit_db`.`drs_object` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- passport_broker
CREATE TABLE IF NOT EXISTS `passport_broker` (
    url VARCHAR(255),
    secret TEXT,
    PRIMARY KEY (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- passport_visa
CREATE TABLE IF NOT EXISTS `passport_visa` (
    id INTEGER AUTO_INCREMENT,
    `name` VARCHAR(255),
    `issuer` VARCHAR(255),
    secret TEXT,
    passport_broker_url VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `pass_url_fk`
		FOREIGN KEY (`passport_broker_url`)
        REFERENCES `starter_kit_db`.`passport_broker` (url)
        ON DELETE NO ACTION
		ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- drs_object_visa
CREATE TABLE `drs_object_visa` (
    drs_object_id VARCHAR(255),
    visa_id INTEGER,
	CONSTRAINT `visa_drs_fk`
		FOREIGN KEY (`drs_object_id`)
		REFERENCES `starter_kit_db`.`drs_object` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `passport_visa_fk`
		FOREIGN KEY (`visa_id`)
		REFERENCES `starter_kit_db`.`passport_visa` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;