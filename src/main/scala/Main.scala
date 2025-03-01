import adapter.{FcmAdapter, ReservationAdapter, ScheduleAdapter, UserAdapter}
import akka.actor.ActorSystem
import application.NotificationService

import scala.concurrent.ExecutionContext
import infra.in.Routes
import config.{FirebaseConfig, Server}
import infra.out.{GrpcReservationClient, GrpcScheduleClient, GrpcUserClient}
import scheduler.NotificationScheduler
import test.TestService

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("notification-system")
    implicit val ec: ExecutionContext = system.dispatcher
    val serviceAccountPath = sys.env.getOrElse("GOOGLE_APPLICATION_CREDENTIALS", "./ncsproject.json")
    FirebaseConfig.initializeFirebase(serviceAccountPath)

    // GRPC Clients 생성
    val grpcScheduleClient = new GrpcScheduleClient("localhost", 50070)
    val grpcReservationClient = new GrpcReservationClient("localhost", 50055)
    val grpcUserClient = new GrpcUserClient("localhost", 50053)

    // Adapters 생성
    val scheduleAdapter = new ScheduleAdapter(grpcScheduleClient)
    val reservationAdapter = new ReservationAdapter(grpcReservationClient)
    val userAdapter = new UserAdapter(grpcUserClient)
    val fcmAdapter = new FcmAdapter()

    // NotificationService 인스턴스 생성 (비즈니스 로직 담당)
    val notificationService = new NotificationService(
      scheduleAdapter,
      reservationAdapter,
      userAdapter,
      fcmAdapter
    )

    // Scheduler 실행 (매일 4AM 실행)
    new NotificationScheduler(notificationService)

    println("Notification Service Started!")

    val testService = new TestService(grpcScheduleClient, grpcReservationClient, grpcUserClient,fcmAdapter)
    testService.runAllTests()

    Server.start(Routes.route)
  }
}