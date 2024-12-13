package factory;

import entidades.Course;
import entidades.Professor;

public class DefaultCourseFactory extends CourseFactory {
    @Override
    public Course createCourse(int id, String name, Professor professor) {
        return new Course(id, name, professor);
    }
}
