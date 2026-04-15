# ProjectGym – Backend

A konditermi webalkalmaz\u00e1s szerveroldali (backend) API-ja. **Spring Boot 3.5** és **Java 17** alapon épül, MySQL adatbázist használ, és JWT alapú hitelesítést biztosít.

## Technológiák

- **Spring Boot 3.5** – fő keretrendszer
- **Java 17**
- **Spring Security** + **JWT (jjwt 0.12.5)** – hitelesítés és jogosultságkezelés
- **Spring Data JPA** – adatbázis-kezelés
- **MySQL** – relációs adatbázis
- **Stripe Java SDK 31.3** – online fizetési integráció
- **Spring Mail** – e-mail értesítések
- **Lombok** – kódcsökkentés
- **Maven** – build eszköz

## Főbb Funkciók (API)

- Felhasználói regisztráció, bejelentkezés, JWT token kezelés
- Szerepkörök: admin, edző, felhasználó
- Foglaláskezelés és naptár logika
- Bérlet, jegy értékesítése
- E-mail értesítések (foglalás visszaigazolás, emlékeztető, lemondás)
- GDPR-kompatibilis adatkezelés, biztonságos jelszótárolás

## Telepítés és Futtatás

### Előfeltételek
- Java 17+
- MySQL adatbázis
- Maven

Az API alapértelmezetten a `http://localhost:8080` címen érhető el.
