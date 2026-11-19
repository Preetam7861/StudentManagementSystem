
package com.student.studentmanagementsystem.service;

import com.student.studentmanagementsystem.dto.ScoreSkillsUpdateRequest;
import com.student.studentmanagementsystem.model.Student;
import com.student.studentmanagementsystem.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        if (studentRepository.existsById(student.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Student ID already exists: " + student.getStudentId());
        }

        if (studentRepository.findByEmailAddressIgnoreCase(
                student.getEmailAddress()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email address already exists");
        }

        validateScores(student.getProgrammingScore(),
                student.getMathematicsScore());

        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with ID: " + id));
    }

    public Student updateScoresAndSkills(
            Integer id, ScoreSkillsUpdateRequest request) {

        Student student = getStudentById(id);

        validateScores(request.getProgrammingScore(),
                request.getMathematicsScore());

        student.setProgrammingScore(request.getProgrammingScore());
        student.setMathematicsScore(request.getMathematicsScore());

        if (request.getTechnicalSkills() != null) {
            student.setTechnicalSkills(request.getTechnicalSkills());
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Integer id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getMeritList() {
        List<Student> students = studentRepository.findAll();

        students.sort(
                Comparator.comparingDouble(Student::getAverageScore)
                        .reversed()
        );

        return students;
    }

    @Transactional(readOnly = true)
    public List<Student> searchByEmail(String email) {
        return studentRepository.findByEmailAddressIgnoreCase(email)
                .map(List::of)
                .orElseGet(List::of);
    }

    @Transactional(readOnly = true)
    public List<Student> searchByCourse(String course) {
        return studentRepository.findByCourseNameIgnoreCase(course);
    }

    // Find technical skills shared by at least two students
    @Transactional(readOnly = true)
    public List<String> findStudentsWithSameTechnicalSkills() {

        Map<String, Long> skillCounts = studentRepository.findAll()
                .stream()
                .filter(student -> student.getTechnicalSkills() != null)
                .flatMap(student ->
                        student.getTechnicalSkills().stream()
                                .filter(skill -> skill != null && !skill.isBlank())
                                .map(skill -> skill.trim().toLowerCase(Locale.ROOT))
                                .distinct()
                )
                .collect(Collectors.groupingBy(
                        skill -> skill,
                        TreeMap::new,
                        Collectors.counting()
                ));

        return skillCounts.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .toList();
    }

    // Programming score below 50 and Mathematics score at least 75
    @Transactional(readOnly = true)
    public List<Student> findProgrammingNeedsImprovementWithGoodMaths() {
        return studentRepository.findAll()
                .stream()
                .filter(student ->
                        student.getProgrammingScore() < 50
                                && student.getMathematicsScore() >= 75
                )
                .toList();
    }

    private void validateScores(Double programming, Double mathematics) {
        if (programming == null || mathematics == null
                || programming < 0 || programming > 100
                || mathematics < 0 || mathematics > 100) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Programming and Mathematics scores must be between 0 and 100"
            );
        }
    }
}
