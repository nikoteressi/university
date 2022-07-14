
# University REST API  (Test task for company Elinext)

### TODO

* Realise CRUD methods for all entities in Application.
* Add end point to get schedule by student and date
* Must start with command **docker-compouse up -d**
* Cover with tests


 End poit to get student schedule **/api/schedules/student/{studentId}/date/{date}**
 
 Date format **YYYY-MM-DD**
 
 ### URI
 
 Method    | URI
------     | ------
**GET:**   |        /api/schedules - returns all schedules
**GET:**   |       /api/schedules/student/{id}/date/{date} - returns student schedule by date
**POST:**  |        /api/schedules - create new schedule
**PUT:**   |          /api/schedules - edit schedule
**DELETE:**|      /api/schedules/{id} - delete schedule by id

**GET:**   |        /api/audiences - returns all audiences
**POST:**  |      /api/audiences - create new audience
**PUT:**   |        /api/audiences - edit audience
**DELETE:** |   /api/audiences/{id} - delete audience by id

**GET:**   |        /api/groups - returns all groups
**POST:**   |     /api/groups - create new group
**PUT:**   |        /api/groups - edit group
**DELETE:** |   /api/groups/{id} - delete group by id

**GET:**   |        /api/lectures - returns all lectures
**POST:**  |      /api/lectures - create new lecture
**PUT:**   |        /api/lectures - edit lecture
**DELETE:** |   /api/lectures/{id} - delete lecture by id

**GET:**   |        /api/students - returns all students
**POST:**   |     /api/students - create new student
**PUT:**    |       /api/students - edit student
**DELETE:**  |  /api/students/{id} - delete student by id

