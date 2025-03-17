package quartz

import org.quartz.spi._
import org.quartz.{Job, Scheduler}
import slick.jdbc.PostgresProfile.api._

class CustomJobFactory(db: Database) extends JobFactory {
  override def newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job = {
    try {
      val jobClass = bundle.getJobDetail.getJobClass
      jobClass.getConstructor(classOf[Database]).newInstance(db)
    } catch {
      case ex: Exception =>
        throw new RuntimeException("Failed to create job instance", ex)
    }
  }
}
