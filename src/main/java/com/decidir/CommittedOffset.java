package com.decidir;

//import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by biandra on 06/09/18.
 */
public class CommittedOffset {

    public Long getOffset(Connection connection, String topic, int partition, int columnId) throws SQLException {
        String sql = "SELECT offset FROM kafka_topic WHERE topic = ? AND partition_name = ? AND column_id = ?";
        PreparedStatement pstatement = connection.prepareStatement(sql);
        pstatement.setString(1, topic);
        pstatement.setInt(2, partition);
        pstatement.setInt(3, columnId);
        ResultSet result = pstatement.executeQuery();
        result.next();
        return result.getLong(1);
    }
}
