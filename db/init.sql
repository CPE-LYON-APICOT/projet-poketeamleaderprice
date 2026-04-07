create table Type (
    id int primary key auto_increment,
    name varchar(255) not null
);

create table Stade (
    id int primary key auto_increment,
    name varchar(255) not null,
    type_id int,
    foreign key (type_id) references Type(id)
);

create table Abilite (
    id int primary key auto_increment,
    name varchar(255) not null,
    description text
);

create table Pokemon (
    id int primary key auto_increment,
    name varchar(255) not null,
    hp int not null,
    attack int not null,
    special_attack int not null,
    defense int not null,
    defense_special int not null,
    speed int not null,
    imageFacePath varchar(255),
    imageDosPath varchar(255),
    imageSpritePath varchar(255),
    description text,
    abilityId int,
    foreign key (abilityId) references Abilite(id)
);

create table Pokemon_Type (
    pokemon_id int,
    type_id int,
    primary key (pokemon_id, type_id),
    foreign key (pokemon_id) references Pokemon(id),
    foreign key (type_id) references Type(id)
);

create table Type_Type_Avantages (
    type_id int,
    avantage_type_id int,
    primary key (type_id, avantage_type_id),
    foreign key (type_id) references Type(id),
    foreign key (avantage_type_id) references Type(id)
);

create table Type_Type_Faiblesses (
    type_id int,
    faiblesse_type_id int,
    primary key (type_id, faiblesse_type_id),
    foreign key (type_id) references Type(id),
    foreign key (faiblesse_type_id) references Type(id)
);

create table Attaque (
    id int primary key auto_increment,
    name varchar(255) not null,
    power int not null,
    accuracy int not null,
    pp int not null,
    type_id int,
    foreign key (type_id) references Type(id)
);

create table HealingItem (
    id int primary key auto_increment,
    name varchar(255) not null,
    hp_heal int not null
);

create table EffectItem (
    id int primary key auto_increment,
    name varchar(255) not null,
    attack int not null,
    special_attack int not null,
    defense int not null,
    defense_special int not null,
    speed int not null
);