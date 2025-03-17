package utils

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import slick.jdbc.H2Profile.api._
import models.Schedule
import database.ScheduleTable

import scala.concurrent.Await
import scala.concurrent.duration._

trait TestBase extends AnyWordSpec with Matchers with ScalatestRouteTest {
  lazy val db = Database.forConfig("h2mem") // In-memory H2 database
  lazy val schedules = ScheduleTable.schedules

  def createSchema(): Unit = {
    Await.result(db.run(schedules.schema.create), 5.seconds)
  }

  def cleanSchema(): Unit = {
    Await.result(db.run(schedules.schema.drop), 5.seconds)
  }

  val testSchedule = Schedule(
    id = None,
    title = "Test Schedule",
    description = "This is a test schedule.",
    date = "2025-03-14",
    startTime = "09:30:00",
    endTime = "10:30:00",
    customerName = "John Doe",
    executed = false,
    creationTime = "2025-03-13T10:00:00"
  )
}
