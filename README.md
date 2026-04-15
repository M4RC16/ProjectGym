# ProjectGym – Frontend

A konditermi webalkalmaz\u00e1s kliensoldali k\u00f3dja. Az alkalmaz\u00e1s **Angular 20** keretrendszerre épül, és **Bootstrap 5** segítségével reszponzív, modern felhasználói felületet biztosít.

## Technológiák

- **Angular 20** – fő keretrendszer
- **Bootstrap 5** + **ng-bootstrap** – UI komponensek és reszponzív rács
- **Font Awesome 7** – ikonok
- **RxJS** – reaktív adatkezelés
- **JWT Decode** – JWT token kezelés a hitelesítéshez
- **TypeScript 5.9**

## Főbb Funkciók (UI)

- Nyitóoldal: képgaléria, felszerelés bemutató, nyitvatartás, árlista
- Regisztráció és bejelentkezés (JWT alapú authentikáció)
- Személyes felhasználói profil (adatok, edzéstörténet, fejlődési grafikon)
- Edzők profiloldalai (bemutatkozás, árazás, naptár)
- Interaktív foglalási naptár (szabad/foglalt időpontok, lemondás)
- Bérlet, jegy és prémium tartalom vásárlása (Stripe integráció)
- Oktatóvideók és termékkatalógus megtekintése
- Admin és edző felület (felhasználókezelés, tartalom szerkesztés, audit-log)
- QR / vonalkódos belépőkód megjelenítés

## Telepítés és Futtatás

### Előfeltételek
- [Node.js](https://nodejs.org/) (LTS verzió ajánlott)
- [Angular CLI](https://angular.dev/tools/cli)

```bash
# Függőségek telepítése
npm install

# Fejlesztői szerver indítása (http://localhost:4200)
npm start

# Production build
npm run build
```

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
