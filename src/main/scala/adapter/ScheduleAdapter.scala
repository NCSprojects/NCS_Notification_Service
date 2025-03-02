package adapter

import application.out.SchedulePort
import domain.ContentSchedule
import infra.out.GrpcScheduleClient

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class ScheduleAdapter(grpcScheduleClient: GrpcScheduleClient)(implicit ec: ExecutionContext) extends SchedulePort {
  override def getScheduleIdsByStartTime(startTime: String): Seq[String] = {
    Await.result(grpcScheduleClient.fetchScheduleIdsByStartTime(startTime), 5.seconds)
  }

  override def getStartTimeByScheduleId(scheduleId: String): String = {
    Await.result(grpcScheduleClient.fetchStartTimeByScheduleId(scheduleId),5.seconds)
  }
}
