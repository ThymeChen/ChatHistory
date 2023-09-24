package cn.thymechen.xiaoming.history.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendMessage {
    long senderCode;
    String senderName;
    long receiverCode;
    String receiverName;

    long time;
    String message;
}
