package routes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import services.AppointmentService
import models.Appointment
import scala.concurrent.ExecutionContext


class AppointmentRoutes(service: AppointmentService)(implicit ec: ExecutionContext) {
  val routes: Route =
    pathPrefix("appointments") {
      concat(
        get {
          parameters(
            "customerNumber".as[Long].?,
            "sortBy".?,
            "sortOrder".?,
            "page".as[Int].?(1),
            "pageSize".as[Int].?(10)
          ) { (customerNumber, sortBy, sortOrder, page, pageSize) =>
            val appointments = service.getAllAppointments(customerNumber, sortBy, sortOrder, page, pageSize)
            onSuccess(appointments) { list =>
              complete(list)
            }
          }
        },
        post {
          entity(as[Appointment]) { appointment =>
            val created = service.createAppointment(appointment)
            onSuccess(created) { id =>
              complete(s"Appointment created with ID: $id")
            }
          }
        },
        put {
          (path(LongNumber) & entity(as[Appointment])) { (id, updatedAppointment) =>
            val updated = service.updateAppointment(id, updatedAppointment)
            onSuccess(updated) {
              case 0 => complete(s"No appointment found with ID: $id")
              case _ => complete(s"Appointment with ID: $id updated successfully")
            }
          }
        },
        delete {
          path(LongNumber) { id =>
            val deleted = service.deleteAppointment(id)
            onSuccess(deleted) {
              case 0 => complete(s"No appointment found with ID: $id")
              case _ => complete(s"Appointment with ID: $id deleted successfully")
            }
          }
        }
      )
    }
}

