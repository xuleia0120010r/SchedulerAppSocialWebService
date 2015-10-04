-- database schedulerapp

create table USER(
ID int primary key auto_increment,
ORG_ID varchar(250) default null,
NAME varchar(250) not null unique,
PASSWORD varchar(250) not null unique,
TOKEN varchar(250) default null,
LOGIN_TIME datetime default null,
GENDER varchar(10) default null,
FAMILY_NAME varchar(250) default null,
GIVEN_NAME varchar(250) default null,
IMAGE_URL varchar(1000) default null,
BIRTHDAY date default null,
EMAIL varchar(500) default null,
LOCALE varchar(100) default null,
LINK varchar(1000) default null,
SOURCE varchar(50) default null,
CREATE_TIME timestamp,
UPDATE_TIME datetime default null
);

insert into USER(NAME,PASSWORD,GENDER,FAMILY_NAME,GIVEN_NAME,EMAIL) 
values('xulei','xulei','male','xu','lei','a0120010@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,FAMILY_NAME,GIVEN_NAME,EMAIL) 
values('xingzibo','xingzibo','male','xing','zibo','a0120528@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,FAMILY_NAME,GIVEN_NAME,EMAIL) 
values('aakanksha','aakanksha','female','bansal','aakanksha','a0120049@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,GIVEN_NAME,EMAIL) 
values('chandrakala','chandrakala','female','chandrakala','a0120619@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,FAMILY_NAME,GIVEN_NAME,EMAIL) 
values('karthik','karthik','male','aiyer','karthik','a0119949@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,GIVEN_NAME,EMAIL) 
values('ramprasath','ramprasath','male','ramprasath','a0119939@nus.edu.sg');
insert into USER(NAME,PASSWORD,GENDER,FAMILY_NAME,GIVEN_NAME,EMAIL) 
values('sruthi','sruthi','female','morusu','sruthi','a0121901@nus.edu.sg');
-- update USER SET STARTTIME='2013-06-29 00:00:00' WHERE NAME='2';


-- assume adding friends with pushing notification is necessary
create table USER_RELATIONSHIP(
ID int primary key auto_increment,
USER_ID int not null, foreign key(USER_ID) references USER(ID),
FRIEND_ID int not null, foreign key(FRIEND_ID) references USER(ID),
constraint USER_ID_FRIEND_ID_UK unique(USER_ID,FRIEND_ID)
);
-- alter table USER_RELATIONSHIP change ENROLLDATE ENROLLDATE timestamp;
-- alter table USER_RELATIONSHIP change PASSWORD PASSWORD varchar(30) not null default '123456';

insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(1,2);  
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(1,3);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(1,5);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(1,6);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(2,1); 
-- for example, (1,2) and (2,1) must be exist at the same time
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(2,5); 
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(2,6);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(3,1);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(3,7);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(4,7);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(5,1);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(5,2);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(5,6);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(6,1);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(6,2);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(6,5);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(6,7);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(7,3);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(7,4);
insert into USER_RELATIONSHIP(USER_ID,FRIEND_ID) values(7,6);


create table USER_LAST_REQUEST_RESULT(
ID int primary key auto_increment,
USER_ID int not null, foreign key(USER_ID) references USER(ID),
TASK varchar(500) not null,
COMPANY_NAME varchar(200) not null,
CATEGORY varchar(500) default null,
ADDRESS varchar(500) not null,
POSTAL_CODE varchar(10) default null,
LAT_LNG varchar(50) not null,
SUBURB varchar(50) not null,
SOURCE_URL varchar(2000) default null,
RATING_VALUE decimal(3,1) default 0,
NUMBER_OF_REVIEWER int default 0,
TASK_TIME datetime default null,
LAST_UPDATE_TIME timestamp
);
-- DROP TABLE USER_LAST_REQUEST_RESULT CASCADE CONSTRAINT PURGE;
-- alter table USER_LAST_REQUEST_RESULT add constraint USER_LAST_REQUEST_RESULT_PK primary key (ID);

insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB,TASK_TIME,SOURCE_URL) 
values(1,'coffee','Starbucks Coffee','Family','Starbucks Coffee, CONNEXIS 1,FUSIONOPOLIS WAY, S(138632)',
'138632','1.299622,103.787656','Queenstown','2015-01-18 14:00:00',
'http://www.onemap.sg/API/services.svc/themesearch?token\u003dg08klh6f8Ul6Mii37xEZFoJkHxAZDiT5A/MUAysQc7Hd0ycOU7yCSgnGnbpf/qwhH1uKodUrxilBcKPaJwKG++HBHpwnHqMni/m6a0Z9IB8oscST30qFeQ\u003d\u003d\u0026wc\u003dsearchval%20like%20\u0027%%\u0027\u0026otptflds\u003dSEARCHVAL,THEME,CATEGORY\u0026returnGeom\u003d1\u0026rset\u003d6895'); 
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB) 
values(1,'115 coffee','115 Coffee','Family','Dover Road Block 33, Market And Food Centre #01-115, 33 Dover Road, 130033',
'130033','1.299598,103.783627','Alexandra');
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB) 
values(1,'seafood pasta','NUS BIZ','Family|Friend','BIZ School, National University of Singapore, 118699',
'118699','1.291249,103.774918','Heng Mui Keng');
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB,TASK_TIME) 
values(2,'coffee','Starbucks Coffee','Family','Starbucks Coffee, CONNEXIS 1,FUSIONOPOLIS WAY, S(138632)',
'138632','1.299622,103.787656','Queenstown','2015-01-18 14:30:00');
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB) 
values(3,'coffee','Starbucks Coffee','Family','Starbucks Coffee, CONNEXIS 1,FUSIONOPOLIS WAY, S(138632)',
'138632','1.299622,103.787656','Queenstown');
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB) 
values(6,'veg salad','NUS BIZ','Family|Friend','BIZ School, National University of Singapore, 118699',
'118699','1.291249,103.774918','Heng Mui Keng');
insert into USER_LAST_REQUEST_RESULT(USER_ID,TASK,COMPANY_NAME,CATEGORY,ADDRESS,POSTAL_CODE,LAT_LNG,SUBURB) 
values(7,'veg salad','NUS BIZ','Family|Friend','BIZ School, National University of Singapore, 118699',
'118699','1.291249,103.774918','Heng Mui Keng');


-- create table for PULL data buffer about add friend requests info
create table FRIEND_REQUEST_BUFFER(
SEND_REQUEST_USER_EMAIL varchar(500) not null, -- foreign key(SEND_REQUEST_USER_EMAIL) references USER(EMAIL), because of MySQL key 767 bytes constraint
RECEIVE_REQUEST_USER_EMAIL varchar(500) not null -- foreign key(RECEIVE_REQUEST_USER_EMAIL) references USER(EMAIL), because of MySQL key 767 bytes constraint
-- constraint SEND_REQUEST_RECEIVE_REQUEST_UK unique(SEND_REQUEST_USER_EMAIL,RECEIVE_REQUEST_USER_EMAIL) because of MySQL key 767 bytes constraint
);

insert into FRIEND_REQUEST_BUFFER(SEND_REQUEST_USER_EMAIL, RECEIVE_REQUEST_USER_EMAIL) values('a0121901@nus.edu.sg','a0120010@nus.edu.sg');

