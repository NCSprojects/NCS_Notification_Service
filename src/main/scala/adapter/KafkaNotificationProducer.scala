package adapter

import application.out.KafkaPort
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

class KafkaNotificationProducer extends KafkaPort{
  private val bootstrapServers = "localhost:9092"
  private val topic = "scheduled_notifications"

  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  private val producer = new KafkaProducer[String, String](props)

  override def sendNotification(scheduleId: String, scheduleTime: Long): Unit = {
    val reminderTime = scheduleTime - 300000 // 5분 전
    val message = s"""{"scheduleId": "$scheduleId", "reminderTime": $reminderTime}"""
    val record = new ProducerRecord[String, String](topic, scheduleId, message)

    producer.send(record)
    println(s"✅ Kafka 메시지 전송 완료: $message")
  }

  def close(): Unit = producer.close()
}