CREATE TABLE `cpus_hotline` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `avg_price` double DEFAULT NULL,
                                `cores` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `frequency` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `l3cache` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `manufacturer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `package_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `prices` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `propositions_quantity` int DEFAULT NULL,
                                `socket_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `threads` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `user_benchmark_cpu_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK8k91s7qdqwthco2gpoxa45ewh` (`user_benchmark_cpu_id`),
                                CONSTRAINT `FK8k91s7qdqwthco2gpoxa45ewh` FOREIGN KEY (`user_benchmark_cpu_id`) REFERENCES `cpus_user_benchmark` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
