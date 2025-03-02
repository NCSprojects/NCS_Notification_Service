package scheduler

import akka.actor.ActorSystem
import usecase.NotificationUseCase

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, ZoneId, ZonedDateTime}
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{MILLISECONDS, _}

class NotificationScheduler(notificationService: NotificationUseCase)(implicit system: ActorSystem, ec: ExecutionContext) {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  // 매일 새벽 4시에 실행되는 스케줄러
  system.scheduler.scheduleAtFixedRate(
    initialDelay = calculateInitialDelay(),
    interval = 24.hours
  ) { () =>
    scheduleDailyContentSchedules()
  }

  // 오늘 진행될 컨텐츠 스케줄 ID 조회 후 알림 예약
  private def scheduleDailyContentSchedules(): Unit = {
    println("새벽 4시: 오늘 진행될 컨텐츠 스케줄 ID 조회 중...")

    val todayStartTime = LocalDateTime.now().format(formatter) // 오늘 날짜 "yyyy-MM-dd 00:00:00"
    val scheduleIds = notificationService.getTodayContentSchedules(todayStartTime)

    scheduleIds.foreach { scheduleId =>
      scheduleNotification(scheduleId)
    }
  }

  // 컨텐츠 스케줄 ID를 기반으로 5분 전 알림 예약
  private def scheduleNotification(scheduleId: String): Unit = {
    val now = System.currentTimeMillis()
    val scheduleTime = notificationService.getScheduleStartTime(scheduleId) // 스케줄 시작 시간 조회
    val reminderTime = scheduleTime - TimeUnit.MINUTES.toMillis(5)
    val delay = reminderTime - now

    if (delay > 0) { // 스케줄 시간 확인
      system.scheduler.scheduleOnce(FiniteDuration(delay, MILLISECONDS)) {
        notificationService.fetchUsersAndSendNotifications(scheduleId)
      }
      println(s"컨텐츠 스케줄 ID: $scheduleId - 5분 전 알림 예약됨")
    } else {
      println(s"이미 지난 컨텐츠 스케줄은 무시됨: $scheduleId")
    }
  }

  // 새벽 4시에 실행되도록 초기 지연 시간 계산 (이미 4AM이 지났으면 내일 4AM으로 설정)
  private def calculateInitialDelay(): FiniteDuration = {
    val now = ZonedDateTime.now(ZoneId.systemDefault())
    val today4AM = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusHours(4)

    val next4AM =
      if (now.isAfter(today4AM)) today4AM.plusDays(1) // 이미 4AM이 지났다면 내일 4AM
      else today4AM

    val delayMillis = next4AM.toInstant.toEpochMilli - System.currentTimeMillis()
    FiniteDuration(delayMillis, MILLISECONDS)
  }
}