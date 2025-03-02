package application.out

trait UserPort {
  def getFcmToken(userId: String): Option[String]
}