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

create table "account" (
	account_id serial unique,
	customer_id int not null,
	balance numeric(10,2) not null,
	constraint pk_accounts primary key  (accountid)
)

--drop table "BankCustomer";
--drop table "Admin";
--drop table "Account";
	
ALTER TABLE "Account" ADD CONSTRAINT "FK_CustomerId"
    FOREIGN KEY ("CustomerId") REFERENCES "BankCustomer" ("CustomerId") ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX "IFK_AccountCustomerId" ON "Account" ("CustomerId");

INSERT INTO "Admin" ("Username", "Password") VALUES ('Bob', '1234');
insert INTO "BankCustomer" ("FirstName", "LastName", "Username", "Password") VALUES ('Blue', 'Berry', 'blue', 'gr8');
