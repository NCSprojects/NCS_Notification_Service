package domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId

case class ContentSchedule(id: String, title: String, startTime: Long)

object ContentSchedule {
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def apply(id: String, title: String, startTime: String): ContentSchedule = {
    val localDateTime = LocalDateTime.parse(startTime, formatter)
    val epochMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
    new ContentSchedule(id, title, epochMillis)
  }
}