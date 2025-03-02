package config

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Server {
  def start(route: Route)(implicit system: ActorSystem, ec: ExecutionContext): Unit = {
    val bindingFuture = Http().newServerAt("localhost", 4000).bind(route)

    println("Server online at http://localhost:4000/")
    StdIn.readLine() // 엔터를 누르면 종료
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}