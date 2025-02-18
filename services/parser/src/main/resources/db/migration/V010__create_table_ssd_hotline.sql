create table ssd_hotline
(
    id                    bigint auto_increment
        primary key,
    avg_price             double       null,
    capacity              varchar(255) null,
    manufacturer          varchar(255) null,
    name                  varchar(255) null,
    prices                varchar(255) null,
    propositions_quantity int          null,
    reading_speed         varchar(255) null,
    type                  varchar(255) null,
    url                   varchar(255) null,
    writing_speed         varchar(255) null
);
