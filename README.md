# Cabalchan
Anonymous forum software

# Demo instance:
www.cabalchan.org

## Building:
./mvnw clean package -DskipTests

## Notes

Cabalchan is still in an extreme alpha state. While usable, it is in need of both (manual) functional testing and automated tests. 
The next step will be writing unit tests and integration tests, while ultimately incorporating something with Github Actions.

## TODO:
1. Custom ban lengths - allow mods to ban for custom lengths not on the dropdown menu
2. Account based notifications - Send and retrieve notifications based on accounts not just sessions. Gives people an incentive to create accounts.
3. Click post id’s in addition to “comments” button - people found comments button confusing.
4. Logged in mods see additional info on regular posts - mods want to browse the site normally and see mod info/options on posts.
5. Mod logs - Log all mod actions and make the modlog accessible.
6. Highlight OP somehow or make it distinct from comments
7. Add ban message to post when banned.
8. Add ability to quote using '<' symbol as well, in a different color, possibly light purple (#b294bb).
9. Change ban lifting from 1 minute to instant
10. Ability to delete notifications
11. Ability to delete announcements (mod)
