package usecase

import domain.ContentSchedule

import scala.concurrent.Future

trait NotificationUseCase {
  def getTodayContentSchedules(startTime: String):  Seq[String]
  def fetchUsersAndSendNotifications(contentScheduleId: String): Future[Unit]
  def getScheduleStartTime(scheduleId: String): Long
  def registScheduleNotification(scheduleId: String, scheduleTime: Long): Unit
}