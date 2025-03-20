create table car (
brand varchar(255) not null ,
model VARCHAR(255) primary KEY,
cost INTEGER);

create table driver (
name varchar(255) primary KEY,
age INTEGER,
drivers_license BOOLEAN not null,
model_car VARCHAR(255) REFERENCES car (model));