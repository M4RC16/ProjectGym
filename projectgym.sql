-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Gép: localhost:3306
-- Létrehozás ideje: 2026. Ápr 14. 14:10
-- Kiszolgáló verziója: 8.4.8
-- PHP verzió: 8.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `projectgym`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `contact_form`
--

CREATE TABLE `contact_form` (
  `id` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- A tábla adatainak kiíratása `contact_form`
--

INSERT INTO `contact_form` (`id`, `email`, `message`, `name`, `phone_number`) VALUES
(1, 'teszt.eszti@email.hu', 'Müködik???', 'Teszt Eszer', '+36301234567');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `galery`
--

CREATE TABLE `galery` (
  `id` int NOT NULL,
  `alt_text` varchar(255) NOT NULL,
  `image_url` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- A tábla adatainak kiíratása `galery`
--

INSERT INTO `galery` (`id`, `alt_text`, `image_url`) VALUES
(1, 'text', 'C:\\uploads\\images\\gallery\\598571ac-9a80-4196-b948-9aa0921ba59c.png');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `refresh_tokens`
--

CREATE TABLE `refresh_tokens` (
  `id` int NOT NULL,
  `device_info` varchar(100) DEFAULT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- A tábla adatainak kiíratása `refresh_tokens`
--

INSERT INTO `refresh_tokens` (`id`, `device_info`, `expiry_date`, `token`, `user_id`) VALUES
(25, 'Unknown Device', '2026-03-17 22:03:24.076123', 'db3e0dd7-de7a-4e37-83fb-5d51c5466a1a', 45),
(26, 'Unknown Device', '2026-03-18 13:57:59.021767', 'ed364e71-c6c3-435e-8cce-5d47f99389a5', 45),
(34, 'Unknown Device', '2026-04-01 13:31:44.841916', 'b7cddeab-ac0e-49dd-8681-e161fa54243b', 29),
(35, 'Unknown Device', '2026-04-01 13:47:14.672440', 'd37f5254-045b-4fb0-ae44-242472c7dcc6', 29),
(36, 'Unknown Device', '2026-04-06 14:12:43.606659', 'e6cf3d06-6c7c-41c6-a9c1-5b35e1bf4431', 46),
(37, 'Unknown Device', '2026-04-08 15:13:54.077257', '2ac0b53e-4edc-4fb1-8bf6-24288fdbe780', 29);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `reservation`
--

CREATE TABLE `reservation` (
  `id` int NOT NULL,
  `scheduled_at` datetime NOT NULL,
  `reservation_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `reservation`
--

INSERT INTO `reservation` (`id`, `scheduled_at`, `reservation_date`) VALUES
(14, '2026-02-13 11:00:00', '2026-02-13 11:00:00'),
(15, '2026-02-13 14:30:00', '2026-02-13 14:00:00'),
(16, '2026-02-13 15:30:00', '2026-02-13 15:00:00'),
(17, '2026-02-14 10:00:00', '2026-02-14 10:00:00'),
(18, '2026-02-14 11:00:00', '2026-02-14 11:00:00'),
(19, '2026-02-14 16:00:00', '2026-02-14 16:00:00'),
(20, '2026-02-15 09:00:00', '2026-02-15 09:00:00'),
(21, '2026-02-15 13:00:00', '2026-02-15 13:00:00'),
(22, '2026-02-15 17:00:00', '2026-02-15 17:00:00'),
(23, '2026-02-24 15:30:00', '2026-02-23 22:57:38');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `reservation_x_user`
--

CREATE TABLE `reservation_x_user` (
  `reservation_x_user_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `reservation_id` int DEFAULT NULL,
  `trainer_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `role`
--

CREATE TABLE `role` (
  `id` int NOT NULL,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `role`
--

INSERT INTO `role` (`id`, `role_name`) VALUES
(1, 'admin'),
(2, 'edző'),
(3, 'felhasználó');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `ticket`
--

CREATE TABLE `ticket` (
  `id` int NOT NULL,
  `ticket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci NOT NULL,
  `validity_length` int NOT NULL,
  `price` int NOT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `ticket`
--

INSERT INTO `ticket` (`id`, `ticket_name`, `validity_length`, `price`, `unit`) VALUES
(2, 'Heti bérlet', 7, 8000, ''),
(3, 'Havi bérlet', 30, 25000, ''),
(4, 'Éves bérlet', 365, 150000, 'Nap');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `profile_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci NOT NULL,
  `birthdate` date DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_hungarian_ci,
  `sex` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `valid_until` date DEFAULT NULL,
  `has_valid_ticket` tinyint(1) DEFAULT NULL,
  `hourly_wage` int DEFAULT NULL,
  `start_shift` time DEFAULT NULL,
  `end_shift` time DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `is_verified` tinyint(1) NOT NULL DEFAULT '0',
  `verification_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `token_expiry_date` datetime(6) DEFAULT NULL,
  `reset_password_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci DEFAULT NULL,
  `reset_token_expiry_date` datetime(6) DEFAULT NULL,
  `role_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- A tábla adatainak kiíratása `user`
--

INSERT INTO `user` (`id`, `first_name`, `last_name`, `profile_picture`, `password`, `email`, `birthdate`, `phone_number`, `description`, `sex`, `valid_until`, `has_valid_ticket`, `hourly_wage`, `start_shift`, `end_shift`, `created_at`, `is_verified`, `verification_token`, `token_expiry_date`, `reset_password_token`, `reset_token_expiry_date`, `role_id`) VALUES
(20, 'bibo', 'jóska', NULL, '$2a$10$qlWu4xm.DE3vIvMQuKFOTurxuR.pKIQpoFmdxI1RhFfJTPfy3bZIK', 'schzsombor@gmail.com', NULL, NULL, 'legjobb edző', NULL, NULL, NULL, 5000, NULL, NULL, '2026-01-09 15:08:43', 1, NULL, NULL, NULL, NULL, 2),
(26, NULL, NULL, NULL, '$2a$10$aE.iD8uycqd.yv5MP8p9Iuqdl08tRiENlLkrKuKZlHGl.QW9pVetC', 'ka@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-15 14:44:30', 1, NULL, NULL, 'Y5AK5Z', '2026-01-15 16:16:40.999099', 2),
(29, 'Gigi', 'Nagy-Putnoki', NULL, '$2a$10$3uRUDwIvz5yct38M76pZWetrcGZpe/7h45mQESeD6dtmE1wPafdzm', 'gigi@gmail.com', NULL, '+bibibibi', 'Kovács Péter 10 éve dolgozik személyi edzőként. Specializálódott az erőnléti edzésre, táplálkozástanra és a rehabilitációs mozgásterápiára. Több mint 200 ügyfélnek segített elérni céljait. Módszertana a fokozatos terhelésépítésen és az egyénre szabott programokon alapul. Szabadidejében ultramaraton versenyeken vesz részt.', NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-21 15:29:41', 1, NULL, NULL, NULL, NULL, 2),
(34, NULL, NULL, NULL, '$2a$10$vZNmzqBUlruWyHspyVxh0OnfzXCTYPlnQ.Di9TqVzxBrRFLwRPXjK', 'ggi@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-26 15:11:10', 1, 'af2ca97f-9bd1-4db4-a577-4440f3c842cd', '2026-01-27 04:11:05.819863', NULL, NULL, 2),
(44, 'John', 'Doe', NULL, '$2a$10$.75pA08f6t12Q.yq5QTvvuSG8DE7AXU4AnenDc0nput6Hw3IBo2jW', 'edzolali@gail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-26 15:55:00', 1, 'd0b05e62-ffdb-4c41-aaa6-23a50ca9261f', '2026-02-27 04:54:57.852002', NULL, NULL, 3),
(45, 'nem tudom', 'segiccs', '/images/fc1214b3-ee5d-4a4e-9daa-481fb6583abb.jpg', '$2a$10$MgIgpD8daVT.sl50ihR71emGHdEfDeQ44jyBA9Vr/DEajcoZ/I6.i', 'valaki@gmail.com', NULL, '+36123456789', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-10 16:58:12', 1, '282b2a8b-1d2d-4924-914d-588af482c088', '2026-03-11 05:58:10.318354', NULL, NULL, 3),
(46, NULL, NULL, '/var/www/projectgym/uploads/images/users/71da9a72-6a44-4453-80b3-97e82ff667a1.jpg', '$2a$10$Rc1Z.NAsw8mgva9lf5EuOOZITf5LBuO7OK9ZgW6hs44aqYroYmsSy', 'titkosvagyok@mail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-03-25 13:55:43', 1, '4e5155ef-3844-4aac-8a09-d2cfbc8a63d7', '2026-03-26 02:55:40.761391', NULL, NULL, 2);

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `contact_form`
--
ALTER TABLE `contact_form`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `galery`
--
ALTER TABLE `galery`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `refresh_tokens`
--
ALTER TABLE `refresh_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKghpmfn23vmxfu3spu3lfg4r2d` (`token`),
  ADD KEY `FKjwc9veyjcjfkej6rnnbsijfvh` (`user_id`);

--
-- A tábla indexei `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKd4u2a38amwjccgjj02bnhwj8u` (`scheduled_at`);

--
-- A tábla indexei `reservation_x_user`
--
ALTER TABLE `reservation_x_user`
  ADD PRIMARY KEY (`reservation_x_user_id`),
  ADD UNIQUE KEY `UK391qaknycxlktvmgj5x7p94r1` (`trainer_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `reservation_id` (`reservation_id`),
  ADD KEY `trainer_id` (`trainer_id`);

--
-- A tábla indexei `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `contact_form`
--
ALTER TABLE `contact_form`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT a táblához `galery`
--
ALTER TABLE `galery`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT a táblához `refresh_tokens`
--
ALTER TABLE `refresh_tokens`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT a táblához `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT a táblához `reservation_x_user`
--
ALTER TABLE `reservation_x_user`
  MODIFY `reservation_x_user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT a táblához `role`
--
ALTER TABLE `role`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT a táblához `ticket`
--
ALTER TABLE `ticket`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT a táblához `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `refresh_tokens`
--
ALTER TABLE `refresh_tokens`
  ADD CONSTRAINT `FKjwc9veyjcjfkej6rnnbsijfvh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Megkötések a táblához `reservation_x_user`
--
ALTER TABLE `reservation_x_user`
  ADD CONSTRAINT `FKae2e4spsmdimpv5j09q59v8sy` FOREIGN KEY (`trainer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `Reservation_x_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `Reservation_x_user_ibfk_2` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON DELETE CASCADE;

--
-- Megkötések a táblához `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
