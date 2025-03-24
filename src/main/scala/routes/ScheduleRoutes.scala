package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import database.ScheduleTable
import models._
import quartz.QuartzScheduler
import slick.jdbc.PostgresProfile.api._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import models.ScheduleJsonProtocol._
import org.quartz._
import spray.json._


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ScheduleRoutes {
  val db = Database.forConfig("mydb")
  val schedules = ScheduleTable.schedules

  def routes: Route =
    pathPrefix("schedules") {
      concat(
        post {
          entity(as[Schedule]) { schedule =>
            println(s"Received schedule: $schedule")
            val insertAction = schedules returning schedules.map(_.id) += schedule
            val dbioTransaction = insertAction.flatMap { id =>
              println(s"Inserted schedule with ID: $id")
              DBIO.from {
                QuartzScheduler.scheduleEvent(schedule.copy(id = Some(id)))
                Future.successful(id)
              }
            }.transactionally
            println(s"Inserting schedule: $schedule")
            onComplete(db.run(dbioTransaction)) {
              case scala.util.Success(_) =>
                println(s"Schedule inserted: $schedule")
                complete(StatusCodes.Created)
              case scala.util.Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },
        get {
          parameters('limit.as[Int].?, 'offset.as[Int].?, 'customerName.?, 'sort.?) { (limit, offset, customerName, sort) =>
            val baseQuery = schedules.filterOpt(customerName) { (schedule, name) =>
              schedule.customerName === name
            }
            val sortedQuery = sort match {
              case Some("asc") => baseQuery.sortBy(_.creationTime.asc)
              case Some("desc") => baseQuery.sortBy(_.creationTime.desc)
              case _ => baseQuery
            }
            val paginatedQuery = sortedQuery.drop(offset.getOrElse(0)).take(limit.getOrElse(10))
            onComplete(db.run(paginatedQuery.result)) {
              case scala.util.Success(data) => complete(ScheduleSeq(data))
              case scala.util.Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },
        put {
          entity(as[Schedule]) { updatedSchedule =>
            val updateAction = schedules.filter(_.id === updatedSchedule.id).update(updatedSchedule)
            onComplete(db.run(updateAction)) {
              case scala.util.Success(result) if result > 0 =>
                QuartzScheduler.scheduleEvent(updatedSchedule)
                complete(StatusCodes.OK, "Schedule updated.")
              case scala.util.Success(_) => complete(StatusCodes.NotFound, "Schedule not found.")
              case scala.util.Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },
        delete {
          parameter("id".as[Int]) { scheduleId =>
            val deleteAction = schedules.filter(_.id === scheduleId).delete
            onComplete(db.run(deleteAction)) {
              case scala.util.Success(result) if result > 0 =>
                QuartzScheduler.scheduler.deleteJob(JobKey.jobKey(s"job-$scheduleId", "events"))
                complete(StatusCodes.OK, "Schedule deleted.")
              case scala.util.Success(_) => complete(StatusCodes.NotFound, "Schedule not found.")
              case scala.util.Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      )
    }
}
