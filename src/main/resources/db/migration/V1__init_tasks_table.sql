CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL,
    done BIT,
    deadline DATETIME NULL,
    created_on DATETIME NULL,
    updated_on DATETIME NULL
);