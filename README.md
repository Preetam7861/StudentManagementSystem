# Student Profile and Skill Development Management System

A Spring Boot + Maven + MySQL project aligned with the supplied CS336B question paper.

## Requirements
- JDK 17 or later
- IntelliJ IDEA
- MySQL Server running locally
- Internet connection for the first Maven dependency download

## Open and run in IntelliJ
1. Extract this ZIP file.
2. In IntelliJ choose **File -> Open** and select the extracted `StudentManagementSystem` folder (the folder containing `pom.xml`).
3. If IntelliJ asks, choose **Load Maven Project** / **Trust Project**.
4. Open `src/main/resources/application.properties`.
5. Change `spring.datasource.password=CHANGE_ME` to your actual MySQL password. Keep username `root` only if that is your MySQL username.
6. Run `database.sql` in MySQL Workbench, or execute `CREATE DATABASE student_db;`.
7. Wait for Maven to finish downloading dependencies.
8. Run `StudentManagementSystemApplication.java`.
9. In the Run console, look for `Started StudentManagementSystemApplication`.

The app runs at `http://localhost:8080`.

## APIs (test with Postman)
### Add a student — POST `/students`
```json
{
  "studentId": 101,
  "studentName": "Aarav Sharma",
  "emailAddress": "aarav@example.com",
  "courseName": "BCA",
  "programmingScore": 88.5,
  "mathematicsScore": 92,
  "technicalSkills": ["Java", "Python", "HTML", "CSS"]
}
```

### Get all students — GET `/students`
### Get one student — GET `/students/101`
### Update scores and skills — PUT `/students/101`
```json
{
  "programmingScore": 91,
  "mathematicsScore": 94,
  "technicalSkills": ["Java", "Spring Boot", "MySQL"]
}
```
### Delete student — DELETE `/students/101`
### Merit list — GET `/students/merit-list`

## Additional Q2 endpoints
- `GET /students/search/email?email=aarav@example.com`
- `GET /students/search/course?course=BCA`
- `GET /students/same-skills`
- `GET /students/programming-improvement`

## Q2 business rules implemented
1. Average score = (Programming + Mathematics) / 2.
2. Performance category: 85+ Excellent; 70–84.99 Good; 50–69.99 Satisfactory; below 50 Needs Improvement.
3. Stronger subject is calculated from the two scores.
4. Search by ID, email, and course.
5. Merit list sorted by average score descending.
6. Students with the same technical-skill set are grouped (skill order and case do not matter).
7. Programming score below 50 and Mathematics score at least 75 are returned by `/students/programming-improvement`.
8. Duplicate student IDs are rejected; scores are validated between 0 and 100.

## Notes
- Student ID is the primary key, so MySQL also prevents duplicate IDs.
- `Student.java` uses encapsulation, a no-argument constructor, a parameterized constructor, getters, setters, `toString`, `equals`, and `hashCode`.
- The technical-skills list is stored in a separate `student_technical_skills` table.
- This is a backend REST API project; it does not include a separate graphical website/frontend because the question paper asks for Spring Boot REST APIs.
- Understand the code and be ready to explain the service logic and Java collections in the viva.
