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
   ```
2. **Configure the Database**:
   - Create a PostgreSQL Database:
     ```sql
     CREATE DATABASE appointmentdb;
     ```
   - Update the database credentials in `src/main/resources/application.conf`:
     ```properties
     slick.dbs.default.db.user = "your_username"
     slick.dbs.default.db.password = "your_password"
     ```
3. **Run Database Migrations**:
     Use the `ScheduleTable` definition to create the `schedules` table. You can generate the schema from Slick or manually define it:
```sql
       CREATE TABLE schedules (
         id SERIAL PRIMARY KEY,
         title TEXT NOT NULL,
         description TEXT NOT NULL,
         date DATE NOT NULL,
         start_time TIME NOT NULL,
         end_time TIME NOT NULL,
         customer_name TEXT NOT NULL,
         executed BOOLEAN NOT NULL,
         creation_time TIMESTAMP NOT NULL
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
   - URL: http://localhost:8080/schedules
   - Request Body:
   ```json
   {
   "title": "Dentist Appointment",
   "description": "Regular check-up",
   "date": "2021-08-01",
   "startTime": "10:00:00",
   "endTime": "11:00:00",
   "customerName": "John Doe",
   "executed": false,
   "creationTime": "2021-07-31T12:00:00"
   }
   ```
2. **Get All Appointments**:  
   - Method: GET
   - URL: http://localhost:8080/schedules

3. **Update Appointment**:  
   - Method: PUT
   - URL: http://localhost:8080/schedules
   - Request Body:
```json
   {
   "id": 1,
   "title": "Updated Dentist Appointment",
   "description": "Updated check-up",
   "date": "2021-08-01",
   "startTime": "10:00:00",
   "endTime": "11:00:00",
   "customerName": "John Doe",
   "executed": false,
   "creationTime": "2021-07-31T12:00:00"
   }
   ```
   - Response: Schedule updated successfully

4. **Delete Appointment**:  
   - Method: DELETE
   - URL: http://localhost:8080/schedules?id=1
   - Response: Schedule deleted successfully

## Testing the API
   You can test the API using tools like Postman, cURL, or any HTTP client of your choice.  Example cURL Commands  
   **Create Appointment**:
```bash
   curl -X POST http://localhost:8080/schedules \
   -H "Content-Type: application/json" \
   -d '{
   "title": "Dentist Appointment",
   "description": "Regular check-up",
   "date": "2021-08-01",
   "startTime": "10:00:00",
   "endTime": "11:00:00",
   "customerName": "John Doe",
   "executed": false,
   "creationTime": "2021-07-31T12:00:00"
   }'
  ```
   **Get All Appointments**:  
```bash
   curl -X GET http://localhost:8080/schedules
   Update Appointment:  
   curl -X PUT http://localhost:8080/schedules \
   -H "Content-Type: application/json" \
   -d '{
   "id": 1,
   "title": "Updated Dentist Appointment",
   "description": "Updated check-up",
   "date": "2021-08-01",
   "startTime": "10:00:00",
   "endTime": "11:00:00",
   "customerName": "John Doe",
   "executed": false,
   "creationTime": "2021-07-31T12:00:00"
   }'
   ```

   **Delete Appointment**:  
```bash   
curl -X DELETE http://localhost:8080/schedules?id=1
   ```
## Future Enhancements
   Enable quartz service clustering
   Implement user authentication and authorization.
   Add unit and integration tests. 
   
## License
   This project is open-source and licensed under the MIT License.