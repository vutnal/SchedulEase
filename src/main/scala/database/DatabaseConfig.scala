package database
import slick.jdbc.PostgresProfile.api._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

object DatabaseConfig {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/appointmentdb")
  hikariConfig.setUsername("your_username")
  hikariConfig.setPassword("your_password")
  hikariConfig.setMaximumPoolSize(10)

  private val dataSource = new HikariDataSource(hikariConfig)
  val db = Database.forDataSource(dataSource, Some(10))
}

