package infra.in

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Routes {
  val route: Route =
    path("hi") {
      get {
        complete("Notification Service is running!")
      }
    }
}