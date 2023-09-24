package cn.thymechen.xiaoming.history.database;

import cn.thymechen.xiaoming.history.data.FriendMessage;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriendTable {
    private final HikariDataSource source;

    private String create = "create table if not exists `%s`.`%s` (" +
            "`id` int unsigned not null auto_increment," +
            "`time` bigint," +
            "`sender_id` bigint," +
            "`receiver_id` bigint," +
            "`message` mediumtext character set utf8mb4 collate utf8mb4_unicode_ci null," +
            "primary key (`id`)," +
            "index `index_time`(`time`)," +
            "index `index_sender`(`sender_id`)," +
            "index `index_receiver`(`receiver_id`)" +
            ");";
    private String insert = "insert into `%s`.`%s` ( `time`, `sender_id`, `receiver_id`, `message` ) values ( ?, ?, ?, ? );";

    public FriendTable(String database, HikariDataSource source) throws SQLException {
        this.source = source;

        create = String.format(create, database, "friend");
        insert = String.format(insert, database, "friend");

        this.create();
    }

    public void create() throws SQLException {
        try(Connection connection = source.getConnection()) {
            connection.createStatement().executeUpdate(create);
        }
    }

    public void insert(FriendMessage friend) throws SQLException {
        try(Connection connection = source.getConnection()) {
            PreparedStatement preparedInsert = connection.prepareStatement(insert);

            preparedInsert.setLong(1, friend.getTime());
            preparedInsert.setLong(2, friend.getSenderCode());
            preparedInsert.setLong(3, friend.getReceiverCode());
            preparedInsert.setString(4, friend.getMessage());

            preparedInsert.executeUpdate();
        }
    }
}
