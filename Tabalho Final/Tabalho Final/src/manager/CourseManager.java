package manager;

import entidades.Course;
import entidades.Professor;
import entidades.Student;
import factory.CourseFactory;
import factory.DefaultCourseFactory;
import iterator.CourseCollection;
import iterator.CourseIterator;
import observer.Observer;
import observer.Subject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CourseManager implements Subject {
    private static CourseManager instance;
    private final CourseCollection courseCollection = new CourseCollection();
    private final List<Student> students = new ArrayList<>();
    private final List<Professor> professors = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();
    private final CourseFactory courseFactory = new DefaultCourseFactory();

    private CourseManager() {
    }

    public static synchronized CourseManager getInstance() {
        if (instance == null) {
            instance = new CourseManager();
        }
        return instance;
    }

    public Course createCourse(int id, String name, Professor professor) {
        Course c = courseFactory.createCourse(id, name, professor);
        courseCollection.addCourse(c);
        notifyObservers();
        return c;
    }

    public Student addStudent(int id, String name) {
        Student s = new Student(id, name);
        students.add(s);
        notifyObservers();
        return s;
    }

    public Professor addProfessor(int id, String name, String formacao) {
        Professor p = new Professor(id, name, formacao);
        professors.add(p);
        notifyObservers();
        return p;
    }

    public CourseCollection getCourseCollection() {
        return courseCollection;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void enrollStudentInCourse(int courseId, int studentId) {
        Student foundStudent = null;
        for (Student s : students) {
            if (s.getId() == studentId) {
                foundStudent = s;
                break;
            }
        }
        if (foundStudent == null) return;
        CourseIterator it = courseCollection.iterator();
        while (it.hasNext()) {
            Course c = it.next();
            if (c.getCourseId() == courseId) {
                c.enrollStudent(foundStudent);
                notifyObservers();
                break;
            }
        }
    }

    public void saveData() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
            for (Professor p : professors) {
                bw.write("PROFESSOR:" + p.getId() + ":" + p.getName() + ":" + p.getFormacao() + "\n");
            }
            CourseIterator it = courseCollection.iterator();
            while (it.hasNext()) {
                Course c = it.next();
                bw.write("COURSE:" + c.getCourseId() + ":" + c.getName() + ":" + c.getProfessor().getId() + "\n");
                for (Student s : c.getEnrolledStudents()) {
                    bw.write("ENROLL:" + c.getCourseId() + ":" + s.getId() + "\n");
                }
            }
            for (Student s : students) {
                bw.write("STUDENT:" + s.getId() + ":" + s.getName() + "\n");
            }
            bw.close();
        } catch (Exception ignored) {
        }
    }

    public void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            List<TempEnroll> tempEnrolls = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                switch (parts[0]) {
                    case "PROFESSOR":
                        addProfessor(Integer.parseInt(parts[1]), parts[2], parts[3]);
                        break;
                    case "COURSE":
                        int cid = Integer.parseInt(parts[1]);
                        String cname = parts[2];
                        int pid = Integer.parseInt(parts[3]);
                        Professor p = findProfessorById(pid);
                        if (p != null) createCourse(cid, cname, p);
                        break;
                    case "STUDENT":
                        addStudent(Integer.parseInt(parts[1]), parts[2]);
                        break;
                    case "ENROLL":
                        int ecid = Integer.parseInt(parts[1]);
                        int esid = Integer.parseInt(parts[2]);
                        tempEnrolls.add(new TempEnroll(ecid, esid));
                        break;
                }
            }
            br.close();
            for (TempEnroll te : tempEnrolls) {
                enrollStudentInCourse(te.courseId, te.studentId);
            }
        } catch (Exception ignored) {
        }
    }

    private Professor findProfessorById(int pid) {
        for (Professor p : professors) {
            if (p.getId() == pid) return p;
        }
        return null;
    }

    public String getAllDataAsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"professors\":[");
        for (int i = 0; i < professors.size(); i++) {
            Professor p = professors.get(i);
            sb.append("{\"id\":").append(p.getId()).append(",")
                    .append("\"name\":\"").append(p.getName()).append("\",")
                    .append("\"formacao\":\"").append(p.getFormacao()).append("\"}");
            if (i < professors.size() - 1) sb.append(",");
        }
        sb.append("],");

        sb.append("\"students\":[");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            sb.append("{\"id\":").append(s.getId()).append(",")
                    .append("\"name\":\"").append(s.getName()).append("\"}");
            if (i < students.size() - 1) sb.append(",");
        }
        sb.append("],");

        sb.append("\"courses\":[");
        var it = courseCollection.iterator();
        boolean firstCourse = true;
        while (it.hasNext()) {
            Course c = it.next();
            if (!firstCourse) sb.append(",");
            sb.append("{");
            sb.append("\"courseId\":").append(c.getCourseId()).append(",");
            sb.append("\"name\":\"").append(c.getName()).append("\",");
            sb.append("\"professor\":{")
                    .append("\"id\":").append(c.getProfessor().getId()).append(",")
                    .append("\"name\":\"").append(c.getProfessor().getName()).append("\",")
                    .append("\"formacao\":\"").append(c.getProfessor().getFormacao()).append("\"},");
            sb.append("\"students\":[");
            List<Student> enrolled = c.getEnrolledStudents();
            for (int j = 0; j < enrolled.size(); j++) {
                Student es = enrolled.get(j);
                sb.append("{\"id\":").append(es.getId()).append(",\"name\":\"").append(es.getName()).append("\"}");
                if (j < enrolled.size() - 1) sb.append(",");
            }
            sb.append("]}");
            firstCourse = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    private static class TempEnroll {
        int courseId;
        int studentId;

        TempEnroll(int c, int s) {
            courseId = c;
            studentId = s;
        }
    }
}
