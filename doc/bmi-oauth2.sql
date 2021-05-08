-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema bmi-oauth2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bmi-oauth2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bmi-oauth2` DEFAULT CHARACTER SET utf8;
USE `bmi-oauth2`;

-- -----------------------------------------------------
-- Table `bmi-oauth2`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`users`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(63)     NOT NULL,
    `password` VARCHAR(255)    NOT NULL,
    `active`   TINYINT         NOT NULL,
    `version`  BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`, `username`)
)
    ENGINE = InnoDB;

CREATE UNIQUE INDEX `id_UNIQUE` ON `bmi-oauth2`.`users` (`id` ASC);

CREATE UNIQUE INDEX `username_UNIQUE` ON `bmi-oauth2`.`users` (`username` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`clients`
(
    `id`               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255)    NOT NULL,
    `oauth2_client_id` VARCHAR(255)    NOT NULL,
    `active`           TINYINT         NOT NULL,
    `version`          BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`, `name`, `oauth2_client_id`)
)
    ENGINE = InnoDB;

CREATE UNIQUE INDEX `id_UNIQUE` ON `bmi-oauth2`.`clients` (`id` ASC);

CREATE UNIQUE INDEX `client_id_UNIQUE` ON `bmi-oauth2`.`clients` (`oauth2_client_id` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`roles`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(255)    NOT NULL,
    `client_id` BIGINT UNSIGNED NOT NULL,
    `version`   BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`, `name`),
    CONSTRAINT `fk_roles_client`
        FOREIGN KEY (`client_id`)
            REFERENCES `bmi-oauth2`.`clients` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE UNIQUE INDEX `id_UNIQUE` ON `bmi-oauth2`.`roles` (`id` ASC);

CREATE INDEX `fk_client_idx` ON `bmi-oauth2`.`roles` (`client_id` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`users_clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`users_clients`
(
    `user_id`   BIGINT UNSIGNED NOT NULL,
    `client_id` BIGINT UNSIGNED NOT NULL,
    `owner`     TINYINT         NOT NULL,
    `version`   BIGINT UNSIGNED NOT NULL,
    CONSTRAINT `fk_users_clients_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `bmi-oauth2`.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_users_clients_client`
        FOREIGN KEY (`client_id`)
            REFERENCES `bmi-oauth2`.`clients` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_user_idx` ON `bmi-oauth2`.`users_clients` (`user_id` ASC);

CREATE INDEX `fk_client_idx` ON `bmi-oauth2`.`users_clients` (`client_id` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`users_roles`
(
    `user_id` BIGINT UNSIGNED NOT NULL,
    `role_id` BIGINT UNSIGNED NOT NULL,
    CONSTRAINT `fk_users_roles_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `bmi-oauth2`.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_users_roles_role`
        FOREIGN KEY (`role_id`)
            REFERENCES `bmi-oauth2`.`roles` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_user_idx` ON `bmi-oauth2`.`users_roles` (`user_id` ASC);

CREATE INDEX `fk_role_idx` ON `bmi-oauth2`.`users_roles` (`role_id` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`resources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`resources`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(255)    NOT NULL,
    `uri`       VARCHAR(255)    NULL,
    `active`    TINYINT         NOT NULL,
    `client_id` BIGINT UNSIGNED NOT NULL,
    `version`   BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_resources_client`
        FOREIGN KEY (`client_id`)
            REFERENCES `bmi-oauth2`.`clients` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE UNIQUE INDEX `id_UNIQUE` ON `bmi-oauth2`.`resources` (`id` ASC);

CREATE INDEX `fk_resources_client_idx` ON `bmi-oauth2`.`resources` (`client_id` ASC);


-- -----------------------------------------------------
-- Table `bmi-oauth2`.`roles_resources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bmi-oauth2`.`roles_resources`
(
    `role_id`     BIGINT UNSIGNED NOT NULL,
    `resource_id` BIGINT UNSIGNED NOT NULL,
    CONSTRAINT `fk_roles_resources_role`
        FOREIGN KEY (`role_id`)
            REFERENCES `bmi-oauth2`.`roles` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_roles_resources_resource`
        FOREIGN KEY (`resource_id`)
            REFERENCES `bmi-oauth2`.`resources` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_roles_resources_role_idx` ON `bmi-oauth2`.`roles_resources` (`role_id` ASC);

CREATE INDEX `fk_roles_resources_resource_idx` ON `bmi-oauth2`.`roles_resources` (`resource_id` ASC);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `bmi-oauth2`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `bmi-oauth2`;
INSERT INTO `bmi-oauth2`.`users` (`id`, `username`, `password`, `active`, `version`)
VALUES (1, 'admin', '$2a$10$JBjifsPVdFsuiuPGsxgJs.p.exCgmAHvRFIHVXyRdqjq3RWQ/gJOW', 1, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bmi-oauth2`.`clients`
-- -----------------------------------------------------
START TRANSACTION;
USE `bmi-oauth2`;
INSERT INTO `bmi-oauth2`.`clients` (`id`, `name`, `oauth2_client_id`, `active`, `version`)
VALUES (1, 'bmi-oauth2-service', 'bmi-oauth2-service', 1, 0);
INSERT INTO `bmi-oauth2`.`clients` (`id`, `name`, `oauth2_client_id`, `active`, `version`)
VALUES (2, 'test client 2', 'client2', 1, 0);
INSERT INTO `bmi-oauth2`.`clients` (`id`, `name`, `oauth2_client_id`, `active`, `version`)
VALUES (3, 'test client 3', 'client3', 1, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bmi-oauth2`.`roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `bmi-oauth2`;
INSERT INTO `bmi-oauth2`.`roles` (`id`, `name`, `client_id`, `version`)
VALUES (1, 'ROLE_ADMIN', 1, 0);
INSERT INTO `bmi-oauth2`.`roles` (`id`, `name`, `client_id`, `version`)
VALUES (2, 'ROLE_USER', 1, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bmi-oauth2`.`users_clients`
-- -----------------------------------------------------
START TRANSACTION;
USE `bmi-oauth2`;
INSERT INTO `bmi-oauth2`.`users_clients` (`user_id`, `client_id`, `owner`, `version`)
VALUES (1, 1, 1, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bmi-oauth2`.`users_roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `bmi-oauth2`;
INSERT INTO `bmi-oauth2`.`users_roles` (`user_id`, `role_id`)
VALUES (1, 1);

COMMIT;

