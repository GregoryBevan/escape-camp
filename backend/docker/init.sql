create database "escape-camp";
create user admin with password 'guess-what';
grant all privileges on database "escape-camp" to admin;
alter database "escape-camp" owner to admin;