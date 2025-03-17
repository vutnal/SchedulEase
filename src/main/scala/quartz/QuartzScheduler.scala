package quartz

import org.quartz._
import org.quartz.impl.StdSchedulerFactory
import models.Schedule
import spray.json._
import java.time.LocalDateTime
import java.sql.Timestamp
import models.ScheduleJsonProtocol._
import slick.jdbc.PostgresProfile.api._

object QuartzScheduler {
  val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler

  def initialize(db: Database): Unit = {
    val jobFactory = new CustomJobFactory(db)
    scheduler.setJobFactory(jobFactory)
    scheduler.start()
  }

  def scheduleEvent(schedule: Schedule): Unit = {
    val eventDateTime = LocalDateTime.of(
      java.time.LocalDate.parse(schedule.date),
      java.time.LocalTime.parse(schedule.startTime)
    )

    if (eventDateTime.isAfter(LocalDateTime.now())) {
      val job = JobBuilder.newJob(classOf[EventJob])
        .withIdentity(s"job-${schedule.id.getOrElse("unknown")}", "events")
        .usingJobData("schedule", schedule.toJson.compactPrint)
        .build()

      val trigger = TriggerBuilder.newTrigger()
        .withIdentity(s"trigger-${schedule.id.getOrElse("unknown")}", "events")
        .startAt(Timestamp.valueOf(eventDateTime))
        .build()

      scheduler.scheduleJob(job, trigger)
      println(s"Scheduled event: ${schedule.title} at $eventDateTime")
    } else {
      println(s"Cannot schedule event: ${schedule.title}, time has passed.")
    }
  }

  def deleteEvent(scheduleId: Int): Unit = {
    scheduler.deleteJob(JobKey.jobKey(s"job-$scheduleId", "events"))
    println(s"Deleted event with ID: $scheduleId")
  }

  def startScheduler(): Unit = {
    scheduler.start()
    println("Quartz Scheduler started.")
  }

  def stopScheduler(): Unit = {
    scheduler.shutdown()
    println("Quartz Scheduler stopped.")
  }
}
