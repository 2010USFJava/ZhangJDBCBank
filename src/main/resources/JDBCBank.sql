create table bank_customer (
	c_id serial primary key,
	first_name varchar(40) not null,
    last_name varchar(40) not null,
    username varchar(20) not null,
    password varchar(20) not null
);

create table admin (
	admin_id serial primary key,
	username varchar(20) not null,
    password varchar(20) not null
);

create table account (
	account_id serial primary key,
	c_id int not null,
	balance numeric(10,2) not null
);

drop table account;

alter table account add constraint fk_customerid
    foreign key (c_id) references bank_customer (c_id) on delete cascade;

create index ifk_accountcid on account (c_id);

insert into admin (username, password) values ('bob', '1234');
insert into bank_customer (first_name, last_name, username, password) values ('blue', 'berry', 'blue', 'gr8');
insert into account (c_id, balance) values (1, 9.20);

create function count_customers() 
	returns bigint as $$
	select count(*) from bank_customer as result;
$$ language sql;

select count_customers();
	