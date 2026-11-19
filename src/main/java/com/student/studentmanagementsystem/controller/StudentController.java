package com.student.studentmanagementsystem.controller;

import com.student.studentmanagementsystem.dto.ScoreSkillsUpdateRequest;
import com.student.studentmanagementsystem.model.Student;
import com.student.studentmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateScoresAndSkills(
            @PathVariable Integer id,
            @Valid @RequestBody ScoreSkillsUpdateRequest request) {

        return studentService.updateScoresAndSkills(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/merit-list")
    public List<Student> getMeritList() {
        return studentService.getMeritList();
    }

    @GetMapping("/search/email")
    public List<Student> searchByEmail(@RequestParam String email) {
        return studentService.searchByEmail(email);
    }

    @GetMapping("/search/course")
    public List<Student> searchByCourse(@RequestParam String course) {
        return studentService.searchByCourse(course);
    }

    @GetMapping("/same-skills")
    public List<String> getStudentsWithSameSkills() {
        return studentService.findStudentsWithSameTechnicalSkills();
    }

    @GetMapping("/programming-improvement")
    public List<Student> getProgrammingImpr$ovementStudents() {
        return studentService.findProgrammingNeedsImprovementWithGoodMaths();
    }
}
