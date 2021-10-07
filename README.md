# Cabalchan
Anonymous forum software (FLOSS - AGPLv3)

# Demo instance:
https://cabalchan.org

## Building:
./mvnw clean package -DskipTests

## Tech Stack
- Architecture: Monolith (with Server Side HTML Templating)
- Front End: Thymeleaf templates, skeleton.css, normalize.css, alpine.js, bootstrap icons, cssgram
- Back End: Java 16+, Spring (Boot), Spring Security, JPA/Hibernate, misc. Java libraries
- Database: PostgreSQL 13.4+

## Notes

Cabalchan is still in an extreme alpha state. While usable, it is in need of both (manual) functional testing and automated tests. 
The next step will be writing unit tests and integration tests, while ultimately incorporating something with Github Actions. See 
changelog for details on updates and proposed changes.

## Special Thanks To:
- The poster only known as 'lewdanon' from leftypol for technical suggestions and bugfixes.
- Krates and other jannies from leftypol for UX and other suggestions.
- Lainchan anons from /λ/
