package adapter


import application.out.FcmPort
import builder.FcmMessageBuilder._
import com.google.firebase.messaging.FirebaseMessaging

import scala.concurrent.{ExecutionContext, Future}

class FcmAdapter()(implicit ec: ExecutionContext) extends FcmPort {

  override def sendPushNotification(token: String, title: String, body: String): Future[String] = Future {
    val notification = title.withBody(body)
    val message = token.withNotification(notification)

    val response = FirebaseMessaging.getInstance().send(message)
    println(s"FCM 푸시 알림 전송 성공: $response")
    response
  }
}