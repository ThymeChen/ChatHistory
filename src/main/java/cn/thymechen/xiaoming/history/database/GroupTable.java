package cn.thymechen.xiaoming.history.database;

import cn.thymechen.xiaoming.history.data.GroupMessage;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GroupTable {
    private final HikariDataSource source;
    private String create = "create table if not exists `%s`.`%s` (" +
            "`id` int unsigned not null auto_increment," +
            "`time` bigint," +
            "`bot_id` bigint not null," +
            "`group_id` bigint not null," +
            "`sender_id` bigint not null," +
            "`message` mediumtext character set utf8mb4 collate utf8mb4_unicode_ci null," +
            "primary key (`id`)," +
            "index `index_time`(`time`)," +
            "index `index_group`(`group_id`)," +
            "index `index_sender`(`sender_id`)" +
            ");",
            insert = "insert into `%s`.`%s` ( `time`, `bot_id`, `group_id`, `sender_id`, `message` ) values ( ?, ?, ?, ?, ? );";

    public GroupTable(String database, HikariDataSource source) throws SQLException {
        this.source = source;

        create = String.format(create, database, "group");
        insert = String.format(insert, database, "group");

        this.create();
    }

    public void create() throws SQLException {
        try(Connection connection = source.getConnection()) {
            connection.createStatement().executeUpdate(create);
        }
    }

    public void insert(GroupMessage message) throws SQLException {
        try(Connection connection = source.getConnection()) {
            PreparedStatement preparedInsert = connection.prepareStatement(insert);

            preparedInsert.setLong(1, message.getTime());
            preparedInsert.setLong(2, message.getBotCode());
            preparedInsert.setLong(3, message.getGroupCode());
            preparedInsert.setLong(4, message.getSenderCode());
            preparedInsert.setString(5, message.getMessage());

            preparedInsert.executeUpdate();
        }
    }
}
