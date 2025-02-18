create table power_supplier_hotline
(
    id                     bigint auto_increment
        primary key,
    avg_price              double       null,
    gpu_connection         varchar(255) null,
    manufacturer           varchar(255) null,
    motherboard_connection varchar(255) null,
    name                   varchar(255) null,
    power                  int          null,
    prices                 varchar(255) null,
    propositions_quantity  int          null,
    standard               varchar(255) null,
    type                   varchar(255) null,
    url                    varchar(255) null
);
