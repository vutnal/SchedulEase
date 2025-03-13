package database
import slick.jdbc.PostgresProfile.api._
import java.time.LocalDateTime
import models.Appointment // Importing the Appointment case class

class Appointments(tag: Tag) extends Table[Appointment](tag, "appointments") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def appointmentTime = column[LocalDateTime]("appointment_time")
  def customerNumber = column[Long]("customer_number")

  def * = (id.?, description, appointmentTime, customerNumber) <> (Appointment.tupled, Appointment.unapply)
}

object AppointmentsTable {
  val appointments = TableQuery[Appointments]
}
