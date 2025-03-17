package database

import slick.jdbc.PostgresProfile.api._
import models.Schedule

class Schedules(tag: Tag) extends Table[Schedule](tag, "schedules") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description")
  def date = column[String]("date")
  def startTime = column[String]("start_time")
  def endTime = column[String]("end_time")
  def customerName = column[String]("customer_name")
  def executed = column[Boolean]("executed")
  def creationTime = column[String]("creation_time")

  // Slick requires this mapping to project rows from DB to `Schedule`
  def * = (id.?, title, description, date, startTime, endTime, customerName, executed, creationTime) <> (Schedule.tupled, Schedule.unapply)
}

object ScheduleTable {
  val schedules = TableQuery[Schedules]
}
