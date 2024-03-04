CREATE TABLE IF NOT EXISTS employee
(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    age int CHECK (age >= 18) NOT NULL
);

CREATE TABLE IF NOT EXISTS department
(
    id BIGSERIAL PRIMARY KEY,
    dpt_name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee_department
(
    employee_id bigint NOT NULL,
    department_id bigint NOT NULL,

    CONSTRAINT pk_empl_depart_id PRIMARY KEY (employee_id, department_id),
    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT fk_department_id FOREIGN KEY (department_id) REFERENCES department (id)
);

CREATE TABLE IF NOT EXISTS phone_numbers
(
    id BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(16) NOT NULL,
    employee_id bigint NOT NULL,

    CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id)
);