package routes

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.Schedule
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import slick.jdbc.H2Profile.api._
import utils.TestBase

class ScheduleRoutesSpec extends TestBase {
  val routes = ScheduleRoutes.routes

  override def beforeAll(): Unit = {
    println("Creating schema ScheduleRoutesSpec")
    createSchema()
  }

  override def afterAll(): Unit = {
    println("Cleaning up schema ScheduleRoutesSpec")
    cleanSchema()
  }

  "Schedule Routes" should {
    "create a new schedule successfully" in {
      val payload =
        """
          |{
          |  "title": "Test Meeting",
          |  "description": "Test Description",
          |  "date": "2025-03-14",
          |  "startTime": "10:00:00",
          |  "endTime": "11:00:00",
          |  "customerName": "Alice Smith",
          |  "executed": false,
          |  "creationTime": "2025-03-13T12:00:00"
          |}
        """.stripMargin

      Post("/schedules", HttpEntity(ContentTypes.`application/json`, payload)) ~> routes ~> check {
        status shouldBe StatusCodes.Created
      }
    }

    "retrieve all schedules" in {
      Get("/schedules") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] should include("Test Meeting")
      }
    }

    "update an existing schedule successfully" in {
      val payload =
        """
          |{
          |  "id": 1,
          |  "title": "Updated Meeting",
          |  "description": "Updated Description",
          |  "date": "2025-03-14",
          |  "startTime": "10:00:00",
          |  "endTime": "11:00:00",
          |  "customerName": "John Smith",
          |  "executed": false,
          |  "creationTime": "2025-03-13T11:00:00"
          |}
        """.stripMargin

      Put("/schedules", HttpEntity(ContentTypes.`application/json`, payload)) ~> routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }

    "delete a schedule successfully" in {
      Delete("/schedules?id=1") ~> routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }
}
