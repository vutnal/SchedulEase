package services

import slick.jdbc.PostgresProfile.api._
import models.Appointment
import database.AppointmentsTable
import scala.concurrent.{ExecutionContext, Future}

class AppointmentService(db: Database)(implicit ec: ExecutionContext) {
  import database.AppointmentsTable._

  def createAppointment(appointment: Appointment): Future[Long] = {
    val query = (appointments returning appointments.map(_.id)) += appointment
    db.run(query)
  }

  def getAllAppointments(
                          customerNumber: Option[Long],
                          sortBy: Option[String],
                          sortOrder: Option[String],
                          page: Int,
                          pageSize: Int
                        ): Future[Seq[Appointment]] = {
    val filteredQuery = customerNumber match {
      case Some(cn) => appointments.filter(_.customerNumber === cn)
      case None     => appointments
    }

    val sortedQuery = sortBy match {
      case Some("appointmentTime") =>
        sortOrder match {
          case Some("desc") => filteredQuery.sortBy(_.appointmentTime.desc)
          case _            => filteredQuery.sortBy(_.appointmentTime.asc)
        }
      case Some("customerNumber") =>
        sortOrder match {
          case Some("desc") => filteredQuery.sortBy(_.customerNumber.desc)
          case _            => filteredQuery.sortBy(_.customerNumber.asc)
        }
      case _ => filteredQuery
    }

    val paginatedQuery = sortedQuery.drop((page - 1) * pageSize).take(pageSize)

    db.run(paginatedQuery.result)
  }

  def updateAppointment(id: Long, updatedAppointment: Appointment): Future[Int] = {
    val query = appointments.filter(_.id === id)
      .map(a => (a.description, a.appointmentTime, a.customerNumber))
      .update((updatedAppointment.description, updatedAppointment.appointmentTime, updatedAppointment.customerNumber))
    db.run(query)
  }

  def deleteAppointment(id: Long): Future[Int] = {
    val query = appointments.filter(_.id === id).delete
    db.run(query)
  }
}

