CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     age INT NOT NULL,
                                     position VARCHAR(100) NOT NULL
);

INSERT INTO users (name, age, position) VALUES
                                            ('Gutsev Oleg', 50, 'Developer'),
                                            ('Ivan Ivanov', 35, 'Designer'),
                                            ('Peter Petrov', 28, 'Manager'),
                                            ('Nata Sidorova', 25, 'Manager');