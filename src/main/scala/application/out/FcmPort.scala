package application.out

import scala.concurrent.Future

trait FcmPort {
  def sendPushNotification(token: String, title: String, body: String): Future[String]
}