package com.student.studentmanagementsystem.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity

@Table(name = "students")
public class Student {

    @Id
    private Integer studentId;

    @NotBlank
    private String studentName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String emailAddress;

    @NotBlank
    private String courseName;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double programmingScore;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double mathematicsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_technical_skills",
            joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> technicalSkills = new ArrayList<>();

    public Student() {
    }

    public Student(Integer studentId, String studentName, String emailAddress,
                   String courseName, Double programmingScore,
                   Double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.emailAddress = emailAddress;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        setTechnicalSkills(technicalSkills);
    }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(Double programmingScore) { this.programmingScore = programmingScore; }

    public Double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(Double mathematicsScore) { this.mathematicsScore = mathematicsScore; }

    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) {
        this.technicalSkills = technicalSkills == null ? new ArrayList<>() : new ArrayList<>(technicalSkills);
    }

    @Transient
    public double getAverageScore() {
        if (programmingScore == null || mathematicsScore == null) return 0.0;
        return (programmingScore + mathematicsScore) / 2.0;
    }

    @Transient
    public String getPerformanceCategory() {
        double average = getAverageScore();
        if (average >= 85) return "Excellent";
        if (average >= 70) return "Good";
        if (average >= 50) return "Satisfactory";
        return "Needs Improvement";
    }

    @Transient
    public String getStrongerSubject() {
        if (programmingScore == null || mathematicsScore == null) return "Not available";
        int comparison = Double.compare(programmingScore, mathematicsScore);
        if (comparison > 0) return "Programming";
        if (comparison < 0) return "Mathematics";
        return "Both are Equal";
    }

    @Override
    public String toString() {
        return "Student{studentId=" + studentId + ", studentName='" + studentName
                + "', emailAddress='" + emailAddress + "', courseName='" + courseName
                + "', programmingScore=" + programmingScore + ", mathematicsScore="
                + mathematicsScore + ", technicalSkills=" + technicalSkills + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Student student)) return false;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
