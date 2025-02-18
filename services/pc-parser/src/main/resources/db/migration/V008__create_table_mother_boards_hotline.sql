create table mother_boards_hotline
(
    id                    bigint auto_increment
        primary key,
    avg_price             double       null,
    case_type             varchar(255) null,
    chipset               varchar(255) null,
    chipset_manufacturer  varchar(255) null,
    manufacturer          varchar(255) null,
    memory_type           varchar(255) null,
    name                  varchar(255) null,
    prices                varchar(255) null,
    propositions_quantity int          null,
    socket_type           varchar(255) null,
    url                   varchar(255) null
);
