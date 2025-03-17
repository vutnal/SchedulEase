package quartz

import models.Schedule
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.H2Profile.api._
import utils.TestBase

class QuartzSchedulerSpec extends TestBase {
//  override def beforeAll(): Unit = {
//    println("Creating schema QuartzSchedulerSpec")
//    createSchema()
//  }

  override def afterAll(): Unit = {
    println("Cleaning up schema QuartzSchedulerSpec")
    cleanSchema()
  }

  "Quartz Scheduler" should {
    "schedule a job successfully" in {
      val scheduler = QuartzScheduler
//      scheduler.startScheduler()

      noException should be thrownBy {
        scheduler.scheduleEvent(testSchedule)
      }

//      scheduler.stopScheduler()
    }

    "delete a job successfully" in {
      val scheduler = QuartzScheduler
//      scheduler.startScheduler()

      scheduler.scheduleEvent(testSchedule)

      noException should be thrownBy {
        scheduler.deleteEvent(1) // Assuming job is scheduled with ID 1
      }

      scheduler.stopScheduler()
    }
  }
}
