package cn.thymechen.xiaoming.history.utility;

import java.util.regex.Pattern;

public class Parttens {
    // Emoji
    public static final Pattern EmojiUnicode = Pattern.compile("([\\x{10000}-\\x{10ffff}\\ud800-\\udfff])");
    public static final Pattern EmojiURL = Pattern.compile("\\[\\[(.*?)]]");

    // Image Base64
    public static final Pattern ImageBase64 = Pattern.compile("\\[base64,(.*?)]");

    // Time
    public static final Pattern Date = Pattern.compile("^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|"+
            "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|"+
            "((0[48]|[2468][048]|[3579][26])00))-02-29)$");
    public static final Pattern Time = Pattern.compile("^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$");
    public static final Pattern FullTime = Pattern.compile("^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|"+
            "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|"+
            "((0[48]|[2468][048]|[3579][26])00))-02-29)," + "([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$");

    // ImageAndFlash
    public static final Pattern Image = Pattern.compile("\\[mirai:image:\\{([A-F0-9]{8})-([A-F0-9]{4})-([A-F0-9]{4})-([A-F0-9]{4})-([A-F0-9]{12})}\\.([a-z]{3,4})]");
    public static final Pattern Flash = Pattern.compile("\\[mirai:flash:\\{([A-F0-9]{8})-([A-F0-9]{4})-([A-F0-9]{4})-([A-F0-9]{4})-([A-F0-9]{12})}\\.([a-z]{3,4})]");
    // At
    public static final Pattern At = Pattern.compile("\\[mirai:at:(\\d+)]");
    // QQ
    public static final Pattern QQ = Pattern.compile("\\d+");
    // AtAll
    public static final Pattern AtAll = Pattern.compile("\\[mirai:atall]");
}
