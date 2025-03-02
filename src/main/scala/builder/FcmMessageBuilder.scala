package builder

import com.google.firebase.messaging.{Message, Notification}

object FcmMessageBuilder {
  implicit class RichNotification(title: String) {
    def withBody(body: String): Notification =
      Notification.builder().setTitle(title).setBody(body).build()
  }

  implicit class RichMessage(token: String) {
    def withNotification(notification: Notification): Message =
      Message.builder().setToken(token).setNotification(notification).build()
  }
}