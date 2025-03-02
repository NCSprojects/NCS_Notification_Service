package adapter.in

import config.KafkaStreamsConfig
import io.circe.generic.auto._
import io.circe.parser._
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{Consumed, KStream, Predicate}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder}
import usecase.NotificationUseCase

import java.util.Properties

case class NotificationMessage(scheduleId: String, reminderTime: Long)

class KafkaNotificationStreams(notificationService: NotificationUseCase) {
  private val topic = "scheduled_notifications"

  def startStream(): Unit = {
    val props: Properties = KafkaStreamsConfig.getProperties
    val builder = new StreamsBuilder()

    // Kafka에서 예약된 알림 메시지를 가져옴
    val sourceStream: KStream[String, String] = builder.stream(topic, Consumed.`with`(Serdes.String(), Serdes.String()))

    // 메시지를 필터링할 때마다 현재 시간을 새로 가져오도록 변경
    val executeNotification: Predicate[String, String] = (_, value) => {
      decode[NotificationMessage](value) match {
        case Right(notification) =>
          val now = System.currentTimeMillis() // ✅ 최신 시간으로 비교
          val delay = notification.reminderTime - now
          println(s"현재 시간: $now, 예약된 알림 시간: ${notification.reminderTime}, 실행 여부: ${delay <= 0}")
          delay <= 0
        case Left(_) => false
      }
    }

    // 실행해야 할 알림 필터링
    val filteredStream = sourceStream.filter(executeNotification)

    // 실행 로직
    filteredStream.foreach { (key, value) =>
      decode[NotificationMessage](value) match {
        case Right(notification) =>
          println(s"5분 전 알림 실행: ${notification.scheduleId}")
          notificationService.fetchUsersAndSendNotifications(notification.scheduleId)
        case Left(error) =>
          println(s"JSON 파싱 실패: $error")
      }
    }

    // Kafka Streams 실행
    val streams = new KafkaStreams(builder.build(), props)
    streams.start()

    // 종료 시 닫기
    sys.addShutdownHook {
      streams.close()
    }
  }
}