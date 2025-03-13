import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import database.DatabaseConfig
import services.AppointmentService
import routes.AppointmentRoutes
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object MainApp extends App {
  implicit val system: ActorSystem = ActorSystem("appointment-system")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContext = system.dispatcher

  val db = DatabaseConfig.db
  val service = new AppointmentService(db)
  val routes = new AppointmentRoutes(service).routes

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)
  println("Server running at http://localhost:8080/")
  println("Press RETURN to stop...")

  StdIn.readLine() // Wait for user input to terminate
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
