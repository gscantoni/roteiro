DROP TABLE IF EXISTS task;
CREATE TABLE task (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(250) NOT NULL,
                      description VARCHAR(250) NOT NULL,
                      completed BOOLEAN
);

