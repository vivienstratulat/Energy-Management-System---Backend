create table device
(
id serial primary key,
description varchar(100) not null,
address varchar(100) not null,
max_hourly_consumption integer not null,
client_id UUID not null
);