package application.out

trait KafkaPort {
  def sendNotification(scheduleId: String, scheduleTime: Long): Unit
}
