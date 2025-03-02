package infra.out

import io.grpc.netty.NettyChannelBuilder
import io.grpc.{ManagedChannel}
import schedulefcm.schedulefcm.{ScheduleRequest, ScheduleResponse, ScheduleServiceGrpc, ScheduleIdRequest, StartTimeResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._

class GrpcScheduleClient(host: String, port: Int)(implicit ec: ExecutionContext) {
  val channel: ManagedChannel = NettyChannelBuilder
    .forAddress(host, port)
    .usePlaintext()
    .build()

  private val stub: ScheduleServiceGrpc.ScheduleServiceStub =
    ScheduleServiceGrpc.stub(channel)

  // 특정 시작 시간에 해당하는 스케줄 ID 목록 조회
  def fetchScheduleIdsByStartTime(startTime: String): Future[Seq[String]] = {
    val request = ScheduleRequest(startTime)

    stub.getScheduleIdsByStartTime(request).map { response: ScheduleResponse =>
      response.scheduleIds
    }
  }

  // 스케줄 ID로 스케줄 시작 시간 조회
  def fetchStartTimeByScheduleId(scheduleId: String): Future[String] = {
    val request = ScheduleIdRequest(scheduleId)

    stub.getStartTimeByScheduleId(request).map { response: StartTimeResponse =>
      response.startTime
    }
  }
}