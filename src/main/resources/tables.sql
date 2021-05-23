CREATE TABLE IF NOT EXISTS `staff`
(
    `uuid` varchar(255) NOT NULL UNIQUE,
    `username` varchar(255) NOT NULL,
    `age` int(11) NOT NULL,
    `description` varchar(255) NOT NULL,
    `rank` int(11) NOT NULL
);