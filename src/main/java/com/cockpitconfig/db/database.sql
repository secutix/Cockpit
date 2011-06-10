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

insert into TIMEFRAME values (0, "Per Minute");
insert into TIMEFRAME values (1, "Per 2 Minute");
insert into TIMEFRAME values (2, "Per hour");
insert into TIMEFRAME values (3, "Per Day");
insert into TIMEFRAME values (4, "Per Month");


drop table if exists CRITERIA;
create table CRITERIA (
        CRI_ID int primary key,
        LABEL varchar(255) not null
);

insert into CRITERIA values (0, "Total no. of Transactions");
insert into CRITERIA values (1, "Total no. of Successful Transactions");
insert into CRITERIA values (2, "Total no. of Unsuccessful Transactions");
insert into CRITERIA values (3, "Unsuccessful Transactions due to Payment failure");
insert into CRITERIA values (4, "Unsuccessful Transactions due to Techincal Glitch");
insert into CRITERIA values (5, "Dropout Rate");

drop table if exists ASSERTIONGROUP;
create table ASSERTIONGROUP (
		ASSERTION_PK int AUTO_INCREMENT primary key,
		LABEL varchar(255),
		COMMUNICATION_ID int not null,
		foreign key (COMMUNICATION_ID) references COMMUNICATION(COMM_ID) on delete cascade
);

drop table if exists ASSERTIONCONDITION;
create table ASSERTIONCONDITION (
		ASSERTIONCOND_PK int not null AUTO_INCREMENT primary key,
		CRITERIA_ID int not null,
		MIN_VALUE int default null,
		MAX_VALUE int default null,
		MIN_DELTA int default null,
		MAX_DELTA int default null,
		TIMEFRAME_ID int not null,
		NOTIFICATION_ID int not null,
		ASSERTIONGROUP_ID int not null,
		ASSERTION_INDEX int not null,
		foreign key (CRITERIA_ID) references CRITERIA(CRI_ID) on delete cascade,
		foreign key (TIMEFRAME_ID) references TIMEFRAME(TIME_ID) on delete cascade,
		foreign key (NOTIFICATION_ID) references NOTIFICATIONLEVEL(NOTI_ID) on delete cascade,
		foreign key (ASSERTIONGROUP_ID) references ASSERTIONGROUP(ASSERTION_PK) on delete cascade
);

drop table if exists NOTIFICATIONOCCURRENCE;
create table NOTIFICATIONOCCURRENCE (
		NOTIOCC_PK int not null AUTO_INCREMENT primary key,
		TIMEOCCUR time not null,
		DATEOCCUR date not null,
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

drop table if exists SOURCES;
create table SOURCES (
		SOURCES_PK int not null AUTO_INCREMENT primary key,
		SOURCE_URL varchar(255) not null,
		DESCRIPTION varchar(255)
);