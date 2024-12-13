package factory;

import entidades.Course;
import entidades.Professor;

public abstract class CourseFactory {
    public abstract Course createCourse(int id, String name, Professor professor);
}
