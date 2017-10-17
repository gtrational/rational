
-- Needs to be run to set up db and user, must be run as root

--
-- Create the database.
--
create database if not exists ratdb default character set 'utf8';

--
-- Create the user.
--
create user if not exists ratuser identified by 'ratuserpwd';


--
-- Grant the user privileges.
--
grant all on ratdb.* to ratuser;