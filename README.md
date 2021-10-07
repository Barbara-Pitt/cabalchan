# Cabalchan
Anonymous forum software (FLOSS - AGPLv3)

# Demo instance:
www.cabalchan.org

## Building:
./mvnw clean package -DskipTests

## Tech Stack
- Architecture: Monolith (with Server Side HTML Templating)
- Front End: Thymeleaf templates, skeleton.css, normalize.css, alpine.js, bootstrap icons, cssgram
- Back End: Java 16+, Spring (Boot), Spring Security, JPA/Hibernate, misc. Java libraries
- Database: PostgreSQL 13.4+

## Notes

Cabalchan is still in an extreme alpha state. While usable, it is in need of both (manual) functional testing and automated tests. 
The next step will be writing unit tests and integration tests, while ultimately incorporating something with Github Actions.

## Updates (October 6, 2021)
1. Ability to show/hide filter and flag options in new and reply post interfaces.
2. Custom ban lengths - allow mods to ban for custom lengths not on the dropdown menu
3. Report button now stays in same tab instead of creating a new one.
4. Reworked notifications - based on cookies not sessions

## TODO:
1. User has Ability to delete notifications
2. Logged in mods see additional info on regular posts - mods want to browse the site normally and see mod info/options on posts.
3. Mod logs - Log all mod actions and make the modlog accessible.
4. Implement search functionality - right now search bar does nothing.
5. Create cabalchan logo, including custom flag & favicon.
