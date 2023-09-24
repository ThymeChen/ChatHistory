package cn.thymechen.xiaoming.history;

import cn.chuanwise.xiaoming.plugin.JavaPlugin;
import cn.thymechen.xiaoming.history.database.DatabaseManager;
import cn.thymechen.xiaoming.history.listeners.HistoryListener;
import cn.thymechen.xiaoming.history.settings.ChatSetting;
import lombok.Getter;

import java.io.File;
import java.sql.SQLException;

public class ChatHistory extends JavaPlugin {
    private final static ChatHistory INSTANCE = new ChatHistory();

    @Getter
    private ChatSetting settings;
    @Getter
    DatabaseManager databaseManager = null;

    public static ChatHistory getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        getDataFolder().mkdir();

        settings = xiaoMingBot.getFileLoader().loadOrSupply(ChatSetting.class, new File(getDataFolder(), "config.json"), ChatSetting::new);

        getLogger().info("正在使用 {} 存储聊天记录", settings.getType());
        if (settings.getType() == ChatSetting.Storage.MYSQL) {
            try {
                getLogger().info("连接数据库...");
                databaseManager = new DatabaseManager(settings.getDatabase());
                getLogger().info("连接成功");
            } catch (SQLException e) {
                getLogger().error("无法连接数据库", e);
            }
        }

        xiaoMingBot.getFileSaver().readyToSave(settings);
    }

    @Override
    public void onEnable() {
        xiaoMingBot.getEventManager().registerListeners(new HistoryListener(), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null)
            databaseManager.close();
    }
}