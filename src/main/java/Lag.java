import com.decidir.CommittedOffset;
import com.decidir.KafkaConsumerMonitor;
import com.decidir.MySQLConnection;
import com.decidir.domain.PartionOffsets;
import com.decidir.domain.TopicPartitionDetail;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by biandra on 06/09/18.
 */
public class Lag {

    private static final Logger LOGGER = LoggerFactory.getLogger(Lag.class);

    private static String user = "spsT_usr";
    private static String password = "veef8Eed";
    private static String host = "localhost";
    private static String port = "3306";
    private static String database = "sps433";
    private static int columnId = 1;
    private static String topic = "legacy-transactions-topic";
    private static String kafkaHost = "localhost:9469,localhost:9155,localhost:9308";

    public static void main(String[] args) {
        KafkaConsumerMonitor monitor = new KafkaConsumerMonitor();
        MySQLConnection mySQLConnection = new MySQLConnection();
        CommittedOffset committedOffset = new CommittedOffset();
        Connection connection = null;
        try {
            connection = mySQLConnection.get(user, password, host, port, database);
            List<TopicPartitionDetail> partitionOffsets = monitor.getLogEndOffset(topic, kafkaHost);
            for (int i = 0; i< partitionOffsets.size(); i++){
                TopicPartitionDetail topicPartitionDetail = partitionOffsets.get(i);
                int partition = topicPartitionDetail.getTopic().partition();
                String topicName = topicPartitionDetail.getTopic().topic();
                Long curretOffset = committedOffset.getOffset(connection, topicName, partition, columnId);
                Long lastOffset = topicPartitionDetail.getLastOffset();
                Long lag = lastOffset - curretOffset;
                LOGGER.info("partition {} - columnId {} - current offset {} - last offset {} - lag {}", partition, columnId, curretOffset, lastOffset, lag);
            }
        } catch (Exception ex){
            LOGGER.error("error to find lag", ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
