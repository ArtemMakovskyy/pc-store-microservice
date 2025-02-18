CREATE TABLE `gpus_hotline` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `avg_price` double DEFAULT NULL,
                                `manufacturer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `memory_size` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `memory_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `port` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `prices` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `propositions_quantity` int DEFAULT NULL,
                                `shina` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                `user_benchmark_gpu_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FKt9if2j4quomejl69leaqth5p4` (`user_benchmark_gpu_id`),
                                CONSTRAINT `FKt9if2j4quomejl69leaqth5p4` FOREIGN KEY (`user_benchmark_gpu_id`) REFERENCES `gpus_user_benchmark` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1087 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
