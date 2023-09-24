package cn.thymechen.xiaoming.history.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessage {
    long groupCode;
    String groupName;
    long botCode;
    String botName;
    long senderCode;
    String senderName;

    long time;
    String message;
}
