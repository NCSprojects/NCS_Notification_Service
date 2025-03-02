package application.out

trait SchedulePort {
  def getScheduleIdsByStartTime(startTime: String): Seq[String]
  def getStartTimeByScheduleId(scheduleId:String): String
}