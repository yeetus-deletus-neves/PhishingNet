create schema dbo;

create table dbo.User(
    id uuid not null,
    username varchar(64) unique not null,
    passwordInfo varchar(256) not null,
    linked_email varchar(200),
    primary key (id)
);

create table dbo.UserToken(
    userID uuid,
    tokenValidationInfo varchar(256) NOT NULL,
    created_at bigint not null,
    last_used_at bigint not null ,
    PRIMARY KEY (tokenValidationInfo),
    FOREIGN KEY (userID) REFERENCES dbo.User(id)
);

create table dbo.RefreshToken(
    id serial not null,
    userID uuid unique not null ,
    rToken varchar(1024),
    primary key (id),
    FOREIGN KEY (userID) REFERENCES dbo.User(id)
);