package models
import java.time.LocalDateTime

case class Appointment(
                        id: Option[Long],
                        description: String,
                        appointmentTime: LocalDateTime,
                        customerNumber: Long
                      )

