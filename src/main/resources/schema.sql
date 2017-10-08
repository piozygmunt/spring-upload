CREATE TABLE IF NOT EXISTS users (
  id int(11) NOT NULL AUTO_INCREMENT ,
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  email VARCHAR(45) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  KEY (id),
  PRIMARY KEY (username));

CREATE TABLE IF NOT EXISTS roles (
  id int(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  KEY (id),
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS user_roles (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(45) NOT NULL,
  role_id int(45) NOT NULL,
  PRIMARY KEY (id),
  KEY (id),
  UNIQUE KEY uni_username_role (role_id,user_id),
  CONSTRAINT fk_username FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_rolename FOREIGN KEY (role_id) REFERENCES roles (id));

/*CREATE USER IF NOT EXISTS 'spring'@'localhost'
  IDENTIFIED BY 'hardPassSpring' PASSWORD EXPIRE;

GRANT ALL PRIVILEGES ON spring.* TO 'spring'@'localhost'
     WITH GRANT OPTION;*/