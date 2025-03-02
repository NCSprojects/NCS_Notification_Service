package config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.{FirebaseApp, FirebaseOptions}
import java.io.FileInputStream

object FirebaseConfig {
  def initializeFirebase(serviceAccountPath: String): Unit = {
    val serviceAccount = new FileInputStream(serviceAccountPath)

    val options = FirebaseOptions.builder()
      .setCredentials(GoogleCredentials.fromStream(serviceAccount))
      .build()

    if (FirebaseApp.getApps.isEmpty) {
      FirebaseApp.initializeApp(options)
      println("Firebase Admin SDK 초기화 완료!")
    } else {
      println("Firebase Admin SDK가 이미 초기화되었습니다.")
    }
  }
}
