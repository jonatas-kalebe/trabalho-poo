package test;

import entidades.Professor;
import manager.CourseManager;
import org.junit.Assert;
import org.junit.Test;

public class CourseManagerTest {
    @Test
    public void testAddCourse() {
        CourseManager manager = CourseManager.getInstance();
        Professor p = manager.addProfessor(999, "Prof Teste", "Doutor");
        manager.createCourse(1001, "TestCourse", p);
        boolean found = false;
        var it = manager.getCourseCollection().iterator();
        while (it.hasNext()) {
            var c = it.next();
            if (c.getName().equals("TestCourse")) found = true;
        }
        Assert.assertTrue(found);
    }

    @Test
    public void testEnrollStudent() {
        CourseManager manager = CourseManager.getInstance();
        Professor p = manager.addProfessor(101, "Prof Test", "Mestre");
        manager.createCourse(1002, "AnotherCourse", p);
        manager.addStudent(5000, "AlunoTeste");
        manager.enrollStudentInCourse(1002, 5000);
        var it = manager.getCourseCollection().iterator();
        boolean enrolled = false;
        while (it.hasNext()) {
            var c = it.next();
            if (c.getCourseId() == 1002) {
                enrolled = c.getEnrolledStudents().stream().anyMatch(s -> s.getId() == 5000);
                break;
            }
        }
        Assert.assertTrue(enrolled);
    }
}
