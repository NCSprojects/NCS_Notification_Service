package infra.out
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import io.grpc.netty.NettyChannelBuilder
import reservationfcm.reservationfcm.{ContentScheduleRequest, ReservationServiceGrpc, UserList}

import scala.concurrent.Future
import scala.jdk.CollectionConverters._

class GrpcReservationClient(reservationServiceHost: String, reservationServicePort: Int) {
  // gRPC 채널 설정
  private val channel: ManagedChannel = NettyChannelBuilder
    .forAddress(reservationServiceHost, reservationServicePort)
    .usePlaintext()
    .build()

  // gRPC blocking stub 생성
  private val stub: ReservationServiceGrpc.ReservationServiceBlockingStub =
    ReservationServiceGrpc.blockingStub(channel)

  // 특정 컨텐츠 스케줄 ID에 예약된 사용자 목록 조회
  def fetchUsersByContentScheduleId(contentScheduleId: String): Seq[String] = {
    val request = ContentScheduleRequest(contentScheduleId)
    val response: UserList = stub.getUsersByContentScheduleId(request)

    response.userIds.toSeq
  }
}
