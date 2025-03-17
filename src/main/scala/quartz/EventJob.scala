package quartz

import org.quartz.{Job, JobExecutionContext}
import spray.json._
import models.{Schedule, ScheduleJsonProtocol}
import database.ScheduleTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class EventJob(db: Database) extends Job {
  import ScheduleJsonProtocol._

  override def execute(context: JobExecutionContext): Unit = {
    val jobData = context.getJobDetail.getJobDataMap
    val scheduleJson = jobData.getString("schedule")
    val schedule = scheduleJson.parseJson.convertTo[Schedule]

    println(s"Executing event: ${schedule.title}")

    // Delete the schedule after execution
    val deleteAction = ScheduleTable.schedules.filter(_.id === schedule.id).delete
    try {
      Await.result(db.run(deleteAction), 10.seconds)
      println(s"Deleted schedule with ID: ${schedule.id.getOrElse("unknown")}")
    } catch {
      case ex: Exception =>
        println(s"Failed to delete schedule: ${ex.getMessage}")
    }
  }
}
