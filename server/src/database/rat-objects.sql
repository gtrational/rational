-- Set up the tables in ratdb

--
-- Table of rat rightings
--
create table if not exists rat_sightings
(
        unique_key              int(11) unsigned        not null    auto_increment
    ,   created_date            bigint(20)              not null
    ,   location_type           text                    not null
    ,   incident_zip            mediumint(9) unsigned   not null
    ,   incident_address        varchar(128)            not null
    ,   city                    varchar(30)             not null
    ,   borough                 varchar(30)             not null
    ,   latitude                decimal(15,13)          not null    default 0.0000000000000
    ,   longitude               decimal(16,13)          not null    default 0.0000000000000
    
    ,   primary key (unique_key)
);

--
-- Table of users
--
create table if not exists users
(
        id                      int(11) unsigned    not null    auto_increment
    ,   email                   varchar(512)        not null    
    ,   password                varchar(512)        not null
    ,   perm_level              tinyint(4)          not null

    , primary key (id)
);


--
-- Table of user sessionIds
--
create table if not exists usersessions
(
        userid                  int(11) unsigned    not null
    ,   sessionid               varchar(512)        not null
    ,   expires                 bigint(20)          not null
    
    ,   primary key (sessionid)
);