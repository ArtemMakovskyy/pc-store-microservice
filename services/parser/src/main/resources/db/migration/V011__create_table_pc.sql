create table pc_config
(
    id                    bigint auto_increment
        primary key,
    avg_gpu_bench         double              null,
    desktop_score         double              null,
    gaming_score          double              null,
    marker                enum ('BEST_PRICE') null,
    prediction_gpu_fpsfhd int                 null,
    price                 decimal(38, 2)      null,
    price_for_fps         int                 null,
    workstation_score     double              null,
    cpu_id                bigint              null,
    gpu_id                bigint              null,
    memory_id             bigint              null,
    motherboard_id        bigint              null,
    power_supplier_id     bigint              null,
    ssd_id                bigint              null,
    constraint FK21q4kmj3ar9rrp92eyr4k9yuq
        foreign key (power_supplier_id) references parser.power_supplier_hotline (id),
    constraint FK3lj9g0yraqxkmompmlhe6n6xo
        foreign key (memory_id) references parser.memory_hotline (id),
    constraint FKgt2fxjg6kveck7g8v8566mjsu
        foreign key (gpu_id) references parser.gpus_hotline (id),
    constraint FKhnncei077qpx1w4cprfdbudly
        foreign key (motherboard_id) references parser.mother_boards_hotline (id),
    constraint FKo56fvbotcqd4pgjcme4ujsxul
        foreign key (ssd_id) references parser.ssd_hotline (id),
    constraint FKskotnel68nh34fw2mlhba7jrd
        foreign key (cpu_id) references parser.cpus_hotline (id)
);
