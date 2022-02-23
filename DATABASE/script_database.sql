-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema warehouse
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `warehouse` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `warehouse` ;

-- -----------------------------------------------------
-- Table `warehouse`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`client` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `warehouse`.`offer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`order` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `clientName` VARCHAR(45) NULL DEFAULT NULL,
  `productName` VARCHAR(45) NULL DEFAULT NULL,
  `pricePerItem` FLOAT NULL,
  `productQuantity` INT NULL,
  `totalPrice` FLOAT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `warehouse`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `warehouse`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `denomination` VARCHAR(45) NULL DEFAULT NULL,
  `brand` VARCHAR(45) NULL DEFAULT NULL,
  `price` FLOAT NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `warehouse`.`client` (`name`, `email`, `address`, `phone`) VALUES ('George', 'george@yahoo.com', 'strada 1', '123');
INSERT INTO `warehouse`.`client` (`name`, `email`, `address`, `phone`) VALUES ('Raul', 'raul@yahoo.com', 'strada 2', '1234');
INSERT INTO `warehouse`.`product` (`denomination`, `brand`, `price`, `quantity`) VALUES ('shampon', 'nivea', '10.90', '20');
INSERT INTO `warehouse`.`product` (`denomination`, `brand`, `price`, `quantity`) VALUES ('vodca', 'stalinskaya', '43.90', '10');
insert into `warehouse`.`client` (`name`, `email`, `address`, `phone`) values ('Robert', 'rob@yahoo.com', 'strada 3', '0722') ;
INSERT INTO `warehouse`.`order` (`clientName`, `productName`, `pricePerItem`, `productQuantity`, `totalPrice`) VALUES ('George1', 'periuta', '8.9', '3', '26.7');