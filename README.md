# Cabalchan
Anonymous forum software (FLOSS - AGPLv3)

# Demo instance:
https://cabalchan.org

## Building:
./mvnw clean package -DskipTests

## Running:
- use java -jar
- Recommend systemd service to run the jar using a bash script on Ubuntu and related distros
- If you run into post-startup lag (usually only for a few minutes), it may be lack of random/entropy java uses for session id generation. Try logging in and doing stuff on the host. Alternatively, running the jar with the '-Djava.security.egd=file:/dev/./urandom' option can help. Finally, dockerized versions of the app seem to be less susceptible to this.

## Tech Stack
- Architecture: Monolith (with Server Side HTML Templating)
- Front End: Thymeleaf templates, skeleton.css, normalize.css, alpine.js, bootstrap icons, cssgram
- Back End: Java 16+, Spring (Boot), Spring Security, JPA/Hibernate, misc. Java libraries
- Database: PostgreSQL 13.4+

## Notes

Cabalchan welcomes all technical and nontechnical suggestions and bugfixes.

## Special Thanks To:
- Krates, Caballo, and other jannies from leftypol for UX and other suggestions.
- Lainchan anons from /λ/
- Various other anons who have helped with testing, and technical bug fixes.
