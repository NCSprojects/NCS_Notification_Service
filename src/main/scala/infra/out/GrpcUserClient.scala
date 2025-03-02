package infra.out
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import io.grpc.netty.NettyChannelBuilder
import userfcm.userfcm.{UserRequest, FcmTokenResponse, UserServiceGrpc}

class GrpcUserClient(userServiceHost: String, userServicePort: Int) {
  // gRPC 채널 설정
  private val channel: ManagedChannel = NettyChannelBuilder
    .forAddress(userServiceHost, userServicePort)
    .usePlaintext()
    .build()

  // gRPC blocking stub 생성
  private val stub = UserServiceGrpc.blockingStub(channel)

  // FCM 토큰 요청 메서드
  def getFcmToken(userId: String): Option[String] = {
    val request = UserRequest(userId)
    val response: FcmTokenResponse = stub.getFcmToken(request)

    Option(response.fcmToken).filter(_.nonEmpty)
  }
}