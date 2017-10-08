CREATE  TABLE IF NOT EXISTS users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));
CREATE TABLE IF NOT EXISTS user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

/*CREATE USER IF NOT EXISTS 'spring'@'localhost'
  IDENTIFIED BY 'hardPassSpring' PASSWORD EXPIRE;

GRANT ALL PRIVILEGES ON spring.* TO 'spring'@'localhost'
     WITH GRANT OPTION;*/