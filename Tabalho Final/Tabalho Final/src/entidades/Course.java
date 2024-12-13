package entidades;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final int courseId;
    private final String name;
    private final Professor professor;
    private final List<Student> enrolledStudents;

    public Course(int courseId, String name, Professor professor) {
        this.courseId = courseId;
        this.name = name;
        this.professor = professor;
        this.enrolledStudents = new ArrayList<>();
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public Professor getProfessor() {
        return professor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void enrollStudent(Student s) {
        enrolledStudents.add(s);
    }

    @Override
    public String toString() {
        return courseId + ") " + name;
    }
}
