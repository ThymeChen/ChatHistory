package cn.thymechen.xiaoming.history.listeners;

import cn.chuanwise.xiaoming.annotation.EventListener;
import cn.chuanwise.xiaoming.event.SimpleListeners;
import cn.thymechen.xiaoming.history.ChatHistory;
import cn.thymechen.xiaoming.history.data.FriendMessage;
import cn.thymechen.xiaoming.history.data.GroupMessage;
import cn.thymechen.xiaoming.history.database.DatabaseManager;
import cn.thymechen.xiaoming.history.settings.ChatSetting;
import cn.thymechen.xiaoming.history.utility.ImageUtil;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class HistoryListener extends SimpleListeners<ChatHistory> {
    ChatSetting settings;
    DatabaseManager databaseManager;

    @Override
    public void onRegister() {
        settings = plugin.getSettings();
        databaseManager = plugin.getDatabaseManager();
    }

    private String convertMessage(MessageChain messageChain) {
        List<SingleMessage> modified = new ArrayList<>(messageChain.size());
        messageChain.forEach(singleMessage -> {
            if (singleMessage instanceof Image) {
                try {
                    singleMessage = new PlainText(ImageUtil.toBase64((Image) singleMessage));
                } catch (IOException e) {
                    getLogger().error("无法将图片转换为 Base64", e);
                }
            }

            modified.add(singleMessage);
        });

        return MiraiCode.serializeToMiraiCode(modified);
    }

    private void insertGroupMessage(GroupMessage message) {
        switch (settings.getType()) {
            case LOG: {
                try {
                    File logDir = new File(plugin.getDataFolder(), "groups");
                    logDir.mkdir();

                    File groupLogDir = new File(logDir, new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                    groupLogDir.mkdir();

                    File groupLogFile = new File(groupLogDir, message.getGroupCode() + ".log");
                    FileWriter writer = new FileWriter(groupLogFile, true);

                    writer.write(String.format("%s [%s(%d)] %s(%d) => %s",
                            new SimpleDateFormat("HH:mm:ss").format(message.getTime()), message.getGroupCode(), message.getGroupName(),
                            message.getSenderName(), message.getSenderCode(), message.getMessage()));
                    writer.write("\n");
                    writer.close();
                } catch (IOException e) {
                    getLogger().error("无法写入群聊记录", e);
                }
                return;
            }
            case CSV: {
                try {
                    File logDir = new File(plugin.getDataFolder(), "groups");
                    logDir.mkdir();

                    File groupLogDir = new File(logDir, new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                    groupLogDir.mkdir();

                    File groupLogFile = new File(groupLogDir, message.getGroupCode() + ".csv");
                    FileWriter writer;
                    if (!groupLogFile.exists()) {
                        writer = new FileWriter(groupLogFile, true);
                        writer.write("time,bot_code,group_code,group_name,sender_code,sender_name,message\n");
                    } else {
                        writer = new FileWriter(groupLogFile, true);
                    }

                    writer.write(String.format("%s,%d,%d,\"%s\",%d,\"%s\",\"%s\"\n",
                            new SimpleDateFormat("HH:mm:ss").format(message.getTime()), message.getBotCode(),
                            message.getGroupCode(), message.getGroupName(), message.getSenderCode(), message.getSenderName(),
                            message.getMessage()));
                    writer.close();
                } catch (IOException e) {
                    getLogger().error("无法写入群聊记录", e);
                }
                return;
            }
            case MYSQL: {
                try {
                    databaseManager.getGroup().insert(message);
                } catch (SQLException e) {
                    getLogger().error("无法向数据库添加记录", e);
                }
                return;
            }
        }
    }

    private void insertFriendMessage(FriendMessage message) {
        switch (settings.getType()) {
            case LOG: {
                try {
                    File logDir = new File(plugin.getDataFolder(), "friends");
                    logDir.mkdir();

                    File friendLogDir = new File(logDir, new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                    friendLogDir.mkdir();

                    File friendLogFile = new File(friendLogDir, (message.getSenderCode() == xiaoMingBot.getCode() ? message.getReceiverCode() : message.getSenderCode()) + ".log");
                    FileWriter writer = new FileWriter(friendLogFile, true);

                    writer.write(String.format("%s [%s(%d) => %s(%d)] %s",
                            new SimpleDateFormat("HH:mm:ss").format(message.getTime()), message.getSenderName(), message.getSenderCode(),
                            message.getReceiverName(), message.getReceiverCode(), message.getMessage()));
                    writer.write("\n");
                    writer.close();
                } catch (IOException e) {
                    getLogger().error("无法写入私聊记录", e);
                }
                return;
            }
            case CSV: {
                try {
                    File logDir = new File(plugin.getDataFolder(), "friends");
                    logDir.mkdir();

                    File friendLogDir = new File(logDir, new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                    friendLogDir.mkdir();

                    File friendLogFile = new File(friendLogDir, (message.getSenderCode() == xiaoMingBot.getCode() ? message.getReceiverCode() : message.getSenderCode()) + ".csv");
                    FileWriter writer;
                    if (!friendLogFile.exists()) {
                        writer = new FileWriter(friendLogFile, true);
                        writer.write("time,sender_code,sender_name,receiver_code,receiver_name,message\n");
                    } else {
                        writer = new FileWriter(friendLogFile, true);
                    }

                    writer.write(String.format("%s,%d,\"%s\",%d,\"%s\",\"%s\"\n",
                            new SimpleDateFormat("HH:mm:ss").format(message.getTime()),
                            message.getSenderCode(), message.getSenderName(), message.getReceiverCode(), message.getReceiverName(),
                            message.getMessage()));
                    writer.close();
                } catch (IOException e) {
                    getLogger().error("无法写入私聊记录", e);
                }
                return;
            }
            case MYSQL: {
                try {
                    databaseManager.getFriend().insert(message);
                } catch (SQLException e) {
                    getLogger().error("无法向数据库添加记录", e);
                }
                return;
            }
        }
    }

    // 监听群聊消息
    @EventListener(listenCancelledEvent = true)
    public void listenGroupMessage(GroupMessageEvent event) {
        long    groupCode   = event.getGroup().getId();
        String  groupName   = event.getGroup().getName();
        long    botCode     = event.getBot().getId();
        String  botName     = event.getBot().getNick();
        long    senderCode  = event.getSender().getId();
        String  senderName  = event.getSenderName();

        if (settings.getExcludeGroup().contains(groupCode))
            return;

        long time = event.getTime() * 1000L;
        String message = convertMessage(event.getMessage());

        insertGroupMessage(new GroupMessage(groupCode, groupName, botCode, botName, senderCode, senderName, time, message));
    }

    // 监听 bot 发出的群聊消息
    @EventListener(listenCancelledEvent = true)
    public void listenBotGroupMessage(GroupMessagePostSendEvent event) {
        long    groupCode   = event.getTarget().getId();
        String  groupName   = event.getTarget().getName();
        long    botCode     = event.getBot().getId();
        String  botName     = event.getBot().getNick();
        long    senderCode  = event.getBot().getId();
        String  senderName  = event.getBot().getNick();

        if (settings.getExcludeGroup().contains(groupCode))
            return;

        long time = System.currentTimeMillis();
        String message = convertMessage(event.getMessage());

        insertGroupMessage(new GroupMessage(groupCode, groupName, botCode, botName, senderCode, senderName, time, message));
    }

    // 监听用户登录 bot 账号后发送的群聊消息
    @EventListener(listenCancelledEvent = true)
    public void listenGroupSyncMessage(GroupMessageSyncEvent event) {
        long    groupCode   = event.getGroup().getId();
        String  groupName   = event.getGroup().getName();
        long    botCode     = event.getBot().getId();
        String  botName     = event.getBot().getNick();
        long    senderCode  = event.getSender().getId();
        String  senderName  = event.getSenderName();

        if (settings.getExcludeGroup().contains(groupCode))
            return;

        long time = event.getTime() * 1000L;
        String message = convertMessage(event.getMessage());

        insertGroupMessage(new GroupMessage(groupCode, groupName, botCode, botName, senderCode, senderName, time, message));
    }

    // 监听私聊消息  bot <- 好友
    @EventListener(listenCancelledEvent = true)
    public void listenFriendMessage(FriendMessageEvent event) {
        long    senderCode   = event.getSender().getId();
        String  senderName   = event.getSenderName();
        long    receiverCode = event.getBot().getId();
        String  receiverName = event.getBot().getNick();

        if (settings.getExcludeFriend().contains(senderCode))
            return;

        long time = event.getTime() * 1000L;
        String message = convertMessage(event.getMessage());

        insertFriendMessage(new FriendMessage(senderCode, senderName, receiverCode, receiverName, time, message));
    }

    // 监听 bot 发出的私聊消息  bot -> 好友
    @EventListener(listenCancelledEvent = true)
    public void listenBotFriendMessage(FriendMessagePostSendEvent event) {
        long    senderCode   = event.getBot().getId();
        String  senderName   = event.getBot().getNick();
        long    receiverCode = event.getTarget().getId();
        String  receiverName = event.getTarget().getNick();

        if (settings.getExcludeFriend().contains(receiverCode))
            return;

        long time = System.currentTimeMillis();
        String message = convertMessage(event.getMessage());

        insertFriendMessage(new FriendMessage(senderCode, senderName, receiverCode, receiverName, time, message));
    }

    // 监听用户登录 bot 账号后发送的私聊消息  bot(其他设备登录) -> 好友
    @EventListener(listenCancelledEvent = true)
    public void listenFriendSyncMessage(FriendMessageSyncEvent event) {
        long    senderCode   = event.getSender().getId();
        String  senderName   = event.getSenderName();
        long    receiverCode = event.getFriend().getId();
        String  receiverName = event.getFriend().getNick();

        if (settings.getExcludeFriend().contains(receiverCode))
            return;

        long time = event.getTime() * 1000L;
        String message = convertMessage(event.getMessage());

        insertFriendMessage(new FriendMessage(senderCode, senderName, receiverCode, receiverName, time, message));
    }
}
