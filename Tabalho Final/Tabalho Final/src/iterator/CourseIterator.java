package iterator;

import entidades.Course;

import java.util.Iterator;
import java.util.List;

public class CourseIterator implements Iterator<Course> {
    private final List<Course> courses;
    private int index;

    public CourseIterator(List<Course> courses) {
        this.courses = courses;
        this.index = 0;
    }

    public boolean hasNext() {
        return index < courses.size();
    }

    public Course next() {
        return courses.get(index++);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
