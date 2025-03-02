package application.out

trait ReservationPort {
  def getUsersByContentScheduleId(contentScheduleId: String): Seq[String]
}