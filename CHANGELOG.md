# Changelog
All changes and proposed changes documented here.

## TODO:
1. User has Ability to delete notifications
2. Logged in mods see additional info on regular posts - mods want to browse the site normally and see mod info/options on posts.
3. Mod logs - Log all mod actions and make the modlog accessible.
4. Implement search functionality - right now search bar does nothing.
5. Create cabalchan logo, including custom flag & favicon.
6. Refactor out and reuse attachment section from entry.html
7. Refactor regexps to use multiline mode: https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#lt
8. Refactor some logic in the controllers into reusable services.

## Updates (October 29, 2021)
1. Updated hyperlink regex to prevent injection attack
2. Sanitize lt/gt inputs in commentutil
3. Added html entity filtering to comment util for security purposes

## Updates (October 27, 2021)
1. Updated youtube regex to prevent js injection attack

## Updates (October 25, 2021)
1. Comment util updated to clean manual youtube/hyperlink bracket syntax for security reasons

## Updates (October 24, 2021)
1. Added up arrows to index view

## Updates (October 23, 2021)
1. Fixed bug with IOexception in new post functionality

## Updates (October 20, 2021)
1. Updated cite regex to not interfere with hyperlinks

## Updates (October 19, 2021)
1. Updated gif handling with apache commons imaging for more edge cases on handling animated gifs