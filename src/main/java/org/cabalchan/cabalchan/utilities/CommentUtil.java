package org.cabalchan.cabalchan.utilities;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class CommentUtil {

    public static String process(String comment) {

        String result = comment.trim();

        //preclean square brackets
        result = result.replaceAll("\\[","%%LBRACKET%%");
        result = result.replaceAll("\\]","%%RBRACKET%%");
        result = result.replaceAll("'","%%QUOTE%%");
        result = result.replaceAll("\"","%%DOUBLEQUOTE%%");
        result = result.replaceAll("<","%%LESSTHAN%%");
        result = result.replaceAll(">","%%GREATERTHAN%%");

        //preclean html entities
        result = result.replaceAll("&([a-z0-9]+|#[0-9]{1,6}|#x[0-9a-fA-F]{1,6});","%%%$1%%%");

        //cite replacement
        result = result.replaceAll("(^|\\s)#([1-9]\\d*)", "$1[entry $2]");
        //purptext replacement
        result = result.replaceAll("(^|[\\n\\r])(%%LESSTHAN%%)([^\\n\\r]*)", "$1[purptext]%%LESSTHAN%%$3[/purptext]");
        //quotetext replacement
        result = result.replaceAll("(^|[\\n\\r])(%%GREATERTHAN%%)([^\\n\\r]*)", "$1[quotetext]%%GREATERTHAN%%$3[/quotetext]");
        //red replacement
        result = result.replaceAll("==(.*?)==", "[redtext]$1[/redtext]");
        //spoiler replacement
        result = result.replaceAll("\\*\\*(.*?)\\*\\*", "[spoiler]$1[/spoiler]");
        //line break(s)
        result = result.replaceAll("(\r?\n){2,}", "[br2]");
        result = result.replaceAll("\r?\n", "[br]");

        //strip html tags
        result = Jsoup.clean(result, Safelist.none());

        //youtube replacement
        result = result.replaceAll("(?:https://)?(?:www\\.)?(?:youtube\\.com)(?:/watch\\?v=)([-\\w]{11})", "[youtube]$1[/youtube]");

        //hyperlink replacement
        result = result.replaceAll("\\b(https?)://[-\\w+&@#/%?=~_|!:,.;(]*[-\\w+&@#/%=~|)]", "[link]$0[/link]");

        //final replacement cites
        result = result.replaceAll("\\[entry ([1-9]\\d*)\\]", "<a href=\"/entry?entryid=$1\"><span>#$1</span></a>");

        //final replacement youtube
        String ytregexp = 
        "<div x-data=\"{ yt: false }\"><span class=\"ytembed\"> "
        + "<span class=\"clickable\" @click=\"yt = !yt\">youtube.com/watch?v=$1[embed]</span> "
        + "<ul x-show=\"yt\"><iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/$1\" "
        + "title=\"YouTube video player\" loading=\"lazy\" frameborder=\"0\" allow=\"accelerometer; autoplay; "
        + "clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></ul></span>"
        + "<a x-show=\"!yt\" href=\"https://iteroni.com/watch?v=$1\" target=\"_blank\">[invidious]</a></div>";
        result = result.replaceAll("\\[youtube\\]([-\\w]{11})\\[/youtube\\]", ytregexp);
        
        //final replacement hyperlinks
        result = result.replaceAll("\\[link\\](.*?)\\[/link\\]", "<a target=\"_blank\" href=\"$1\">$1</a>");

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

        //final replace brackets
        result = result.replaceAll("%%LBRACKET%%","&#91;");
        result = result.replaceAll("%%RBRACKET%%","&#93;");

        //final replace quotes
        result = result.replaceAll("%%QUOTE%%","&#39;");
        result = result.replaceAll("%%DOUBLEQUOTE%%","&#34;");

        //final replace lt/gt
        result = result.replaceAll("%%LESSTHAN%%","&lt;");
        result = result.replaceAll("%%GREATERTHAN%%","&gt;");

        //final replace entity literals
        result = result.replaceAll("%%%([a-z0-9]+|#[0-9]{1,6}|#x[0-9a-fA-F]{1,6})%%%","&amp;$1&#59;");
        
        return result;
    }
}
