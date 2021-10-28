package org.cabalchan.cabalchan.utilities;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class CommentUtil {

    public static String process(String comment) {

        String result = comment.trim();
        //tag cleaning
        result = result.replaceAll("\\[youtube\\]","");
        result = result.replaceAll("\\[/youtube\\]","");
        result = result.replaceAll("\\[/byoutube\\]","");
        result = result.replaceAll("\\[link\\]","");
        result = result.replaceAll("\\[/link\\]","");
        result = result.replaceAll("\\[/blink\\]","");
        //break tag replacement
        result = result.replaceAll("\\[br\\]","");
        result = result.replaceAll("\\[br2\\]","");
        //cite replacement
        result = result.replaceAll("(^|\\s)#([1-9]\\d*)", "$1[entry $2]");
        //youtube replacement
        result = result.replaceAll("(?:https://)?(?:www\\.)?(?:youtube\\.com)(?:/watch\\?v=)([^\"&?\\/\s]{11})", "[youtube]$1[/youtube][invidious]$1[/invidious]");
        //hyperlink replacement
        result = result.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;(]*[-a-zA-Z0-9+&@#/%=~_|)]", "[link]$0[/blink]$0[/link]");
        //purptext replacement
        result = result.replaceAll("(^|[\\n\\r])(<)([^\\n\\r]*)", "$1[purptext]&lt;$3[/purptext]");
        //quotetext replacement
        result = result.replaceAll("(^|[\\n\\r])(>)([^\\n\\r]*)", "$1[quotetext]&gt;$3[/quotetext]");
        //red replacement
        result = result.replaceAll("==(.*?)==", "[redtext]$1[/redtext]");
        //spoiler replacement
        result = result.replaceAll("\\*\\*(.*?)\\*\\*", "[spoiler]$1[/spoiler]");
        //line break(s)
        result = result.replaceAll("(\r?\n){2,}", "[br2]");
        result = result.replaceAll("\r?\n", "[br]");
        //strip html tags
        result = Jsoup.clean(result, Safelist.none());

        //final replacement cites
        result = result.replaceAll("\\[entry ([1-9]\\d*)\\]", "<a href=\"/entry?entryid=$1\"><span>#$1</span></a>");

        //final replacement youtube
        result = result.replaceAll("\\[youtube\\](.*?)\\[/youtube\\]", 
        "<div x-data=\"{ yt: false }\"><span class=\"ytembed\"> <span class=\"clickable\" @click=\"yt = !yt\">youtube.com/watch?v=$1[embed]</span> <ul x-show=\"yt\"><iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/$1\" title=\"YouTube video player\" loading=\"lazy\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></ul></span>");
        //invidious link for non youtube users
        result = result.replaceAll("\\[invidious\\]([^\"&?\\/\s]{11})\\[/invidious\\]", "<a x-show=\"!yt\" href=\"https://iteroni.com/watch?v=$1\" target=\"_blank\">[invidious]</a></div>");
        //final replacement hyperlinks
        result = result.replaceAll("\\[link\\]", "<a target=\"_blank\" href=\"");
        result = result.replaceAll("\\[/blink\\]", "\">");
        result = result.replaceAll("\\[/link\\]", "</a>");

        //final replacement break
        result = result.replaceAll("\\[br\\]", "<br>");
        result = result.replaceAll("\\[br2\\]", "<br><br>");

        //final replacement spoiler
        result = result.replaceAll("\\[spoiler\\](.*?)\\[/spoiler\\]", "<span class='spoiler'>$1</span>");

        //final replacement quotetext
        result = result.replaceAll("\\[quotetext\\](.*?)\\[/quotetext\\]", "<span class='quotetext'>$1</span>");

        //final replacement purptext
        result = result.replaceAll("\\[purptext\\](.*?)\\[/purptext\\]", "<span class='purptext'>$1</span>");

        //final replacement redtxt
        result = result.replaceAll("\\[redtext\\](.*?)\\[/redtext\\]", "<span class='redtext'>$1</span>");
        return result;
    }
}
