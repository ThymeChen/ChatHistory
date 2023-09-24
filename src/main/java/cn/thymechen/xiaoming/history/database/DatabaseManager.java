package cn.thymechen.xiaoming.history.database;

import cn.thymechen.xiaoming.history.settings.ChatSetting;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.SQLException;

public class DatabaseManager {
    private final HikariDataSource source;

    @Getter
    private final GroupTable group;
    @Getter
    private final FriendTable friend;

    public DatabaseManager(ChatSetting.Database database) throws SQLException {
        HikariConfig databaseConfig = new HikariConfig();

        // 设置数据库信息
        databaseConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        databaseConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8&autoReconnect=true",
                database.getHost(), database.getPort(), database.getDatabase()));
        databaseConfig.setUsername(database.getUsername());
        databaseConfig.setPassword(database.getPassword());

        // 设置连接池配置
        databaseConfig.setMaximumPoolSize(database.getMaximumPoolSize());
        databaseConfig.setIdleTimeout(database.getIdleTimeout());
        databaseConfig.setMaxLifetime(database.getMaxLifetime());

        source = new HikariDataSource(databaseConfig);

        group = new GroupTable(database.getDatabase(), source);
        friend = new FriendTable(database.getDatabase(), source);
    }

    public void close() {
        if (source.isRunning())
            source.close();
    }
}
