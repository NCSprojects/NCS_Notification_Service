package config

import org.apache.kafka.streams.StreamsConfig
import java.util.Properties

object KafkaStreamsConfig {
  val bootstrapServers = "localhost:9092"
  val applicationId = "notification-streams"

  def getProperties: Properties = {
    val props = new Properties()
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde")
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serdes$StringSerde")
//    // Kafka 내부 DEBUG 로그 비활성화
//    props.put("log4j.logger.org.apache.kafka", "INFO")
//    props.put("log4j.logger.org.apache.kafka.streams", "INFO")
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "5000") // 5초마다 offset commit
    props.put(StreamsConfig.POLL_MS_CONFIG, "5000") // 5초마다 poll 수행

    props
  }
}