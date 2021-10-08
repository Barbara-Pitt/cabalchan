# Changelog
All changes and proposed changes documented here.

## Updates (October 8, 2021)
1. Refactored Mod controller into several smaller controllers
2. Modified gitignore to ignore the log text file.

## Updates (October 7, 2021)
1. Fixed typo in rules ('commerical' instead of 'commercial')
2. Newline squeezing now allows two breaks for paragraphs.
3. Users can no longer manually enter the break square bracket tag, evading newline squeezing.
4. purple/quote text now puts captured newline before span
5. purple/quote text no longer prematurely terminates on dollar sign
6. added ability to do cross entry cites using the hash symbol followed by the post number
7. Refactored main controller into into smaller controllers
8. Modified hyperlink regex to allow parens
9. Modified application properties to allow caching of statics
10. Updated cookie/user uuid to last for 365 days max
11. Added file logging

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
6. Refactor out and reuse attachment section from entry.html
7. Refactor regexps to use multiline mode: https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#lt
8. Refactor mod controller into smaller controllers
9. Refactor some logic in the controllers into reusable services.

## Special Thanks To:
- The poster only known as 'lewdanon' from leftypol for technical suggestions and bugfixes.
- Krates and other jannies from leftypol for UX and other suggestions.
- Lainchan anons from /λ/
