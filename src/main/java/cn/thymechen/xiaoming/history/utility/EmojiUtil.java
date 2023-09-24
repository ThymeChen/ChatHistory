package cn.thymechen.xiaoming.history.utility;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class EmojiUtil {
    public static String emojiEncoder(String message) {
        Matcher matcher = Parttens.EmojiUnicode.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb,
                    "[[" + URLEncoder.encode(matcher.group(1), StandardCharsets.UTF_8) + "]]");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String emojiDecoder(String message) {
        Matcher matcher = Parttens.EmojiURL.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb,
                    URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
