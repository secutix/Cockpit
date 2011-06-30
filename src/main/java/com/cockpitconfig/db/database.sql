drop database if exists Cockpit;
create database Cockpit;
use Cockpit;

drop table if exists COMMUNICATION;
create table COMMUNICATION (
        COMM_ID int primary key,
        LABEL varchar(255) not null
);

insert into communication values (0, "E-Mail");
insert into communication values (1, "Nagios");

drop table if exists NOTIFICATIONLEVEL;
create table NOTIFICATIONLEVEL (
        NOTI_ID int primary key,
        LABEL varchar(255) not null
);

insert into NOTIFICATIONLEVEL values (0, "INFO");
insert into NOTIFICATIONLEVEL values (1, "WARNING");
insert into NOTIFICATIONLEVEL values (2, "ALERT");

drop table if exists ASSERTIONSTATE;
create table ASSERTIONSTATE (
        ASS_ID int primary key,
        LABEL varchar(255) not null
);

insert into ASSERTIONSTATE values (0, "Disable");
insert into ASSERTIONSTATE values (1, "Enable");

drop table if exists TIMEFRAME;
create table TIMEFRAME (
        TIME_ID int primary key,
        LABEL varchar(255) not null
);

insert into TIMEFRAME values (0, "per step");
insert into TIMEFRAME values (1, "per 5 step");
insert into TIMEFRAME values (2, "per 10 step");
insert into TIMEFRAME values (3, "per 25 step");
insert into TIMEFRAME values (4, "per 50 step");

drop table if exists SOURCES;
create table SOURCES (
		SOURCES_PK int not null AUTO_INCREMENT primary key,
		SOURCE_URL text not null,
		DESCRIPTION varchar(255)
);

drop table if exists ASSERTIONGROUP;
create table ASSERTIONGROUP (
		ASSERTION_PK int AUTO_INCREMENT primary key,
		LABEL varchar(255),
		SOURCE int not null,
		COMMUNICATION_ID int not null,
		foreign key (SOURCE) references SOURCES(SOURCES_PK),
		foreign key (COMMUNICATION_ID) references COMMUNICATION(COMM_ID) on delete cascade
);

drop table if exists ASSERTIONCONDITION;
create table ASSERTIONCONDITION (
		ASSERTIONCOND_PK int not null AUTO_INCREMENT primary key,
		STREAM text not null,
		MIN_VALUE BIGINT default null,
		MAX_VALUE BIGINT default null,
		MIN_DELTA BIGINT default null,
		MAX_DELTA BIGINT default null,
		TIMEFRAME_ID int not null,
		NOTIFICATION_ID int not null,
		ASSERTIONGROUP_ID int not null,
		ASSERTION_INDEX int not null,
		foreign key (TIMEFRAME_ID) references TIMEFRAME(TIME_ID) on delete cascade,
		foreign key (NOTIFICATION_ID) references NOTIFICATIONLEVEL(NOTI_ID) on delete cascade,
		foreign key (ASSERTIONGROUP_ID) references ASSERTIONGROUP(ASSERTION_PK) on delete cascade
);

drop table if exists NOTIFICATIONOCCURRENCE;
create table NOTIFICATIONOCCURRENCE (
		NOTIOCC_PK int not null AUTO_INCREMENT primary key,
		TIMEOCCUR time,
		DATEOCCUR date,
		ASSERTIONCONDITION_PK int,
		ALERTTYPE varchar(255),
		DESCRIPTION varchar(255),
		foreign key (ASSERTIONCONDITION_PK) references ASSERTIONCONDITION(ASSERTIONCOND_PK) on delete cascade
);

drop table if exists TIMECONSTRAINTS;
create table TIMECONSTRAINTS (
		TIME_PK int not null AUTO_INCREMENT primary key,
		ASSGRP_ID int not null,
		MONDAY boolean not null default 0,
		TUESDAY boolean not null default 0,
		WEDNESDAY boolean not null default 0,
		THURSDAY boolean not null default 0,
		FRIDAY boolean not null default 0,
		SATURDAY boolean not null default 0,
		SUNDAY boolean not null default 0,
		START_HOUR int not null,
		START_MIN int not null,
		END_HOUR int not null,
		END_MIN int not null,
		foreign key (ASSGRP_ID) references ASSERTIONGROUP(ASSERTION_PK) on delete cascade
);

drop table if exists ACTIONS;
create table ACTIONS (
		ACTIONS_PK int not null AUTO_INCREMENT primary key,
		ASSERTIONGROUP_PK int not null,
		EMAIL varchar(255),
		NAGIOS varchar(255),
		foreign key (ASSERTIONGROUP_PK) references ASSERTIONGROUP(ASSERTION_PK) on delete cascade
);

drop table if exists COMMUNICATIONVIAEMAIL;
create table COMMUNICATIONVIAEMAIL (
		COMMEMAIL_PK int not null AUTO_INCREMENT primary key,
		ASSGROUP_ID int not null,
		EMAILRECIPENTS varchar(255),
		foreign key (ASSGROUP_ID) references ASSERTIONGROUP(ASSERTION_PK) on delete cascade
);