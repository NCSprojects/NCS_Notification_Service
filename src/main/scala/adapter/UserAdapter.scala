package adapter

import application.out.UserPort
import infra.out.GrpcUserClient

class UserAdapter(userClient: GrpcUserClient) extends UserPort {

  override def getFcmToken(userId: String): Option[String] = {
    userClient.getFcmToken(userId)
  }
}