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
9. Look into storing width/height data with images as to not have jumping around with content reflows upon lazy loading images.

## Updates (October 15, 2021)
1. Updated FAQ with link to page explaining filters.
2. Image attachments now store height/width information to prevent content reflows upon lazy loading. CSS tweaked slightly.

## Updates (October 15, 2021)
1. Added distinct class to category text
2. Added "Latest" button to allow users to see all posts (incl. replies), not just OP's/threads
3. Added x-cloak to prevent images and videos from being shown before alpine can load and display spoilered images/videos.

## Updates (October 14, 2021)
1. Increased spoiler(text) time to transition from 1 second to 1.5 seconds.

## Updates (October 13, 2021)
1. Addded categories (lightweight boards) functionality.
2. Spoilered IP addresses in mod menu