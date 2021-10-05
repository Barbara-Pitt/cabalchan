# Cabalchan
Anonymous forum software

# Demo instance:
www.cabalchan.org

## Building:
./mvnw clean package -DskipTests

## Notes

Cabalchan is still in an extreme alpha state. While usable, it is in need of both (manual) functional testing and automated tests. 
The next step will be writing unit tests and integration tests, while ultimately incorporating something with Github Actions.

## Updates (October 5, 2021)
1. There is now the ability to quote using '<' symbol as well, in a light purple (#b294bb) color.
2. Changed ban lifting from 1 minute to instant.
3. Highlight OP and make it distinct from comments - OP's now have orange borders on bottom and top, and orange postid. Clicking OP's postid also opens reply section.
4. Click post id’s in addition to “comments” button for links/replies.
5. Ability to delete announcements (mod) on announcements page.
6. Added 'instagram' style filter options for image attachments
7. Banning now adds message to post when banned.

## TODO:
1. Custom ban lengths - allow mods to ban for custom lengths not on the dropdown menu
2. Account based notifications - Send and retrieve notifications based on accounts not just sessions. Gives people an incentive to create accounts.
3. Logged in mods see additional info on regular posts - mods want to browse the site normally and see mod info/options on posts.
4. Mod logs - Log all mod actions and make the modlog accessible.
5. Ability to delete notifications
