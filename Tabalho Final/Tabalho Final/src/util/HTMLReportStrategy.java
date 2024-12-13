package util;

import entidades.Course;
import entidades.Professor;
import entidades.Student;
import iterator.CourseCollection;
import iterator.CourseIterator;

public class HTMLReportStrategy implements ReportStrategy {
    @Override
    public String generateReport(CourseCollection courses) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Course Report</title></head><body>");
        sb.append("<h1>Course Report</h1>");
        sb.append("<table border='1'><tr><th>Course ID</th><th>Name</th><th>Professor</th><th>Students</th></tr>");
        CourseIterator it = courses.iterator();
        while (it.hasNext()) {
            Course c = it.next();
            sb.append("<tr>");
            sb.append("<td>").append(c.getCourseId()).append("</td>");
            sb.append("<td>").append(c.getName()).append("</td>");
            Professor p = c.getProfessor();
            sb.append("<td>").append(p.getName()).append(" (").append(p.getFormacao()).append(")</td>");
            sb.append("<td>");
            for (Student s : c.getEnrolledStudents()) {
                sb.append(s.getId()).append(" - ").append(s.getName()).append("<br/>");
            }
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }
}
