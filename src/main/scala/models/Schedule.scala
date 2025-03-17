package models

import spray.json._

case class Schedule(
                     id: Option[Int],        // Matches `id.?`
                     title: String,          // Matches `title`
                     description: String,    // Matches `description`
                     date: String,           // Matches `date`
                     startTime: String,      // Matches `startTime`
                     endTime: String,        // Matches `endTime`
                     customerName: String,   // Matches `customerName`
                     executed: Boolean,      // Matches `executed`
                     creationTime: String    // Matches `creationTime`
                   )

case class ScheduleSeq(schedules: Seq[Schedule])

object ScheduleJsonProtocol extends DefaultJsonProtocol {
  implicit val scheduleFormat: RootJsonFormat[Schedule] = jsonFormat9(Schedule)
  implicit val schedulesFormat: RootJsonFormat[ScheduleSeq] = jsonFormat1(ScheduleSeq)
}



