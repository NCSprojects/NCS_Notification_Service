package adapter

import application.out.ReservationPort
import infra.out.GrpcReservationClient

class ReservationAdapter(grpcReservationClient: GrpcReservationClient) extends ReservationPort {
  override def getUsersByContentScheduleId(contentScheduleId: String): Seq[String] = {
    grpcReservationClient.fetchUsersByContentScheduleId(contentScheduleId)
  }
}