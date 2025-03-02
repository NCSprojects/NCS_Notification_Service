package config
import com.google.auth.oauth2.{GoogleCredentials, ServiceAccountCredentials}
import java.io.{FileInputStream, File}
import scala.util.{Try, Success, Failure}

object FcmAuth {
  private val FCM_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"

  def getAccessToken(serviceAccountPath: String): Option[String] = {
    Try {
      val credentials = ServiceAccountCredentials
        .fromStream(new FileInputStream(new File(serviceAccountPath)))
        .createScoped(java.util.Collections.singleton(FCM_SCOPE))

      credentials.refreshIfExpired()
      credentials.getAccessToken.getTokenValue
    } match {
      case Success(token) => Some(token)
      case Failure(exception) =>
        println(s"FCM Access Token 가져오기 실패: ${exception.getMessage}")
        None
    }
  }
}
