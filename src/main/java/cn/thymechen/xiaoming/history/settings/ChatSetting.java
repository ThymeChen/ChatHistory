package cn.thymechen.xiaoming.history.settings;

import cn.chuanwise.toolkit.preservable.AbstractPreservable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatSetting extends AbstractPreservable {
    public enum Storage {
        LOG,
        CSV,
        MYSQL
    }

    final String _type = "数据存储方式，可选 LOG, CSV, MYSQL，分别为 .log 格式，.csv 格式，MySQL 数据库";
    Storage type = Storage.CSV;

    List<Long> excludeGroup = new ArrayList<>();
    List<Long> excludeFriend = new ArrayList<>();

    final String _database0 = "数据库相关配置，当 type = MYSQL 时生效";
    final String _database1 = "注意！请将数据库字符集设置为 utf8mb4 来启用完整的 UTF-8！";
    Database database = new Database();

    @Getter
    public static class Database {
        String host = "127.0.0.1";
        int port = 3306;
        String database = "database_name";
        String username = "root";
        String password = "password";

        final String _maximumPoolSize = "连接池中最大连接数";
        int maximumPoolSize = 10;
        final String _idleTimeout = "连接在池中闲置的最长时间，单位：毫秒/ms";
        int idleTimeout = 600000;
        final String _maxLifetime = "连接池中连接的最大生存期，单位：毫秒/ms";
        int maxLifetime = 1800000;
    }
}
