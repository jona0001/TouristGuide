CREATE DATABASE touristGuide;
use touristGuide;
CREATE table tourist_attraction
(
    ID             int primary key auto_increment not null,
    name           varchar(255)                   not null,
    description    varchar(255)                   not null,
    price          double,
    convertedPrice double,
    cityID int,
    foreign key (cityID) REFERENCES city(ID)
);

CREATE table tag
(
    ID  int primary key auto_increment not null,
    tag varchar (255)
);

CREATE TABLE city
(
    ID   int primary key auto_increment not null,
    city varchar (255)
);

CREATE TABLE attractions_tags(
                                 cityID int,
                                 tagID int,
                                 foreign key (cityID) REFERENCES city(ID),
                                 foreign key (tagID) REFERENCES  tag(ID)
);
