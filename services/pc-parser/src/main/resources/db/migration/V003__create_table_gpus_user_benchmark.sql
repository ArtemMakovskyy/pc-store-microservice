CREATE TABLE `gpus_user_benchmark` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `avg_bench` double DEFAULT NULL,
                                       `is_deleted` bit(1) NOT NULL,
                                       `manufacturer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                       `model` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                       `price` double DEFAULT NULL,
                                       `user_rating` double DEFAULT NULL,
                                       `value_percents` double DEFAULT NULL,
                                       `url_of_gpu` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                       `model_hl` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                                       `power_requirement` int DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2302 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
