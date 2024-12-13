package iterator;

import entidades.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseCollection {
    private final List<Course> courses = new ArrayList<>();

    public void addCourse(Course c) {
        courses.add(c);
    }

    public CourseIterator iterator() {
        return new CourseIterator(courses);
    }

    public List<Course> getAllCourses() {
        return courses;
    }
}
