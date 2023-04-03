create database "escape-camp";
create user admin with password 'ochometh';
grant all privileges on database "escape-camp" to admin;
alter database "escape-camp" owner to admin;