drop table if exists designation;
create table designation
(
    id   int primary key auto_increment,
    name varchar(255)
);

drop table if exists employee;
create table employee
(
    id int primary key
);

drop table if exists holiday;
create table holiday
(
    id            int primary key auto_increment,
    version       int,
    description   varchar(255),
    observed_date DATE,
    official_date DATE
);

drop table if exists type;
create table type
(
    id          int,
    version     int,
    description varchar(255)
);

drop table if exists holiday_type;
create table holiday_type
(
    type_id    int,
    holiday_id int,
    foreign key (type_id) references type (id),
    foreign key (holiday_id) references holiday (id)
);

drop table if exists role;
create table role
(
    id        int,
    version   int,
    authority varchar(255)
);
