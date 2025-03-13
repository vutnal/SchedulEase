# Appointment Management System

This project is a **Scala-based appointment management system** built using **Akka HTTP** for the REST API, **Slick** for database interaction, and **PostgreSQL** as the database backend. 
The system supports creating, updating, retrieving, and deleting appointments, along with powerful filtering, sorting, and pagination features.

## Features
- RESTful API for managing appointments
- Supports filtering by customer number
- Sorting by appointment time or customer number (ascending/descending)
- Pagination to handle large datasets
- PostgreSQL for persistent data storage
- Slick for type-safe database interaction
- Easy-to-configure database connection


## Prerequisites
- **Scala** (2.13 or later)
- **SBT** (Scala Build Tool)
- **PostgreSQL** (installed and running)
- Java Development Kit (JDK 8+)

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/vutnal/SchedulEase
   cd ScheduleEase

2. **Configure the Database**: 
- Create a PostgreSQL Database:
  ```postgres-sql
  CREATE DATABASE appointmentdb;
  ```
  - Update the database credentials in src/main/resources/application.conf:
      ```hocon
      slick.dbs.default.db.user = "your_username"
      slick.dbs.default.db.password = "your_password"
      ```
3. **Run Database Migrations**: 
- Use the AppointmentsTable definition to create the appointments table. You can generate the schema from Slick or manually define it:
   
   ```postgres-sql
             CREATE TABLE appointments (
                 id SERIAL PRIMARY KEY,
                 description TEXT NOT NULL,
                 appointment_time TIMESTAMP NOT NULL,
                 customer_number BIGINT NOT NULL
            );
 ```
4. **Run the Application**:
   ```bash
   sbt run
   ```
    The application will start on http://localhost:8080.

## API Endpoints
1. **Create Appointment**:
   - Method: POST
   - URL: http://localhost:8080/appointments
   - Request Body:
     ```json
     {
       "description": "Dentist Appointment",
       "appointmentTime": "2021-08-01T10:00:00",
       "customerNumber": 1234567890
     }
     ```
2. **Get All Appointments**:
   - Method: GET
   - URL: http://localhost:8080/appointments
   - Query Parameters:
     - customerNumber (optional): Filter by customer number
     - sortBy (optional): Sort by appointmentTime or customerNumber
     - sortOrder (optional): asc (default) or desc
     - page (optional): Page number (default: 1)
     - pageSize (optional): Number of items per page (default: 10)
3. **Update Appointment**: 
   - Method: PUT
   - URL: http://localhost:8080/appointments/{id}
   - Request Body:
     ```json
     {
       "description": "Dentist Appointment",
       "appointmentTime": "2021-08-01T10:00:00",
       "customerNumber": 1234567890
     }
     ```
   - Response: Appointment with ID: 1 updated successfully
4. **Delete Appointment**: 
   - Method: DELETE
   - URL: http://localhost:8080/appointments/{id}
   - Response: Appointment with ID: 1 deleted successfully

## Testing the API
You can test the API using tools like Postman, cURL, or any HTTP client of your choice.

**Example cURL Commands**
- Create Appointment:
   ```bash
  curl -X POST http://localhost:8080/appointments \
       -H "Content-Type: application/json" \
       -d '{"description": "Dentist Appointment", "appointmentTime": "2021-08-01T10:00:00", "customerNumber": 1234567890}'
  ```
- Get All Appointments:
   ```bash
    curl -X GET http://localhost:8080/appointments
    ```
- Update Appointment:
    ```bash
     curl -X PUT http://localhost:8080/appointments/1 \
            -H "Content-Type: application/json" \
            -d '{"description": "Dentist Appointment", "appointmentTime": "2021-08-01T10:00:00", "customerNumber": 1234567890}'
     ```
- Delete Appointment:
    ```bash
    curl -X DELETE http://localhost:8080/appointments/1
    ```
## Future Enhancements
Add email/SMS notifications for reminders.

Implement user authentication and authorization.

Add unit and integration tests.

## License
This project is open-source and licensed under the MIT License.