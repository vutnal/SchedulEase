package services
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import models.Appointment
import database.{Appointments, AppointmentsTable}
import slick.jdbc.H2Profile.api._
import services.AppointmentService
import slick.dbio.DBIO
import slick.lifted.TableQuery

class AppointmentServiceSpec extends AsyncFlatSpec with Matchers with BeforeAndAfter {

  var db: Database = _
  var service: AppointmentService = _

  before {
    db = Database.forConfig("h2mem")
    val appointments = TableQuery[Appointments]

    val setup = DBIO.seq(
      appointments.schema.create,
      appointments += Appointment(Some(1), "Doctor's Visit", java.time.LocalDateTime.now(), 12345),
      appointments += Appointment(Some(2), "Client Meeting", java.time.LocalDateTime.now().plusDays(1), 67890)
    )
    db.run(setup)
    service = new AppointmentService(db)
  }

  after {
    db.close()
  }

  "createAppointment" should "add a new appointment to the database" in {
    val newAppointment = Appointment(None, "Team Meeting", java.time.LocalDateTime.now().plusDays(2), 11111)
    service.createAppointment(newAppointment).map { id => id shouldBe  2L}
  }

  "getAllAppointments" should "retrieve all appointments from the database" in {
    service.getAllAppointments(None, None, None, 1, 10).map { appointments =>
      appointments.length shouldBe 3
    }
  }
}
