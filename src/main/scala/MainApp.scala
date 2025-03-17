import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import routes.ScheduleRoutes
import quartz.QuartzScheduler

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("ScheduleSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  // Start Quartz Scheduler
  QuartzScheduler.initialize(ScheduleRoutes.db)

  // Start Akka HTTP server
  val bindingFuture = Http().newServerAt("localhost", 8080).bind(ScheduleRoutes.routes)
  println("Server started at http://localhost:8080/")

  // Graceful shutdown
  sys.addShutdownHook {
    QuartzScheduler.stopScheduler()
    bindingFuture.flatMap(_.unbind())
    system.terminate()
  }
}
