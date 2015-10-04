create table FRIEND_REQUEST_BUFFER(
ID int primary key auto_increment,
SEND_REQUEST_USER_EMAIL varchar(500) not null,
RECEIVE_REQUEST_USER_EMAIL varchar(500) not null
);

insert into FRIEND_REQUEST_BUFFER(SEND_REQUEST_USER_EMAIL, RECEIVE_REQUEST_USER_EMAIL) 
values('a0121901@nus.edu.sg', 'a0120010@nus.edu.sg');
