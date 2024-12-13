package util;

import entidades.Course;
import entidades.Professor;
import entidades.Student;
import iterator.CourseCollection;
import iterator.CourseIterator;

public class JSONReportStrategy implements ReportStrategy {

    private static final String INDENT = "    "; // 4 spaces indentation

    @Override
    public String generateReport(CourseCollection courses) {
        StringBuilder sb = new StringBuilder();
        CourseIterator it = courses.iterator();

        sb.append("[\n");
        int level = 1;
        boolean first = true;

        while (it.hasNext()) {
            Course c = it.next();
            if (!first) {
                sb.append(",\n");
            }
            indent(sb, level).append("{\n");
            int innerLevel = level + 1;
            indent(sb, innerLevel).append("\"courseId\": ").append(c.getCourseId()).append(",\n");
            indent(sb, innerLevel).append("\"name\": \"").append(c.getName()).append("\",\n");
            Professor p = c.getProfessor();
            indent(sb, innerLevel).append("\"professor\": {\n");
            int profLevel = innerLevel + 1;
            indent(sb, profLevel).append("\"id\": ").append(p.getId()).append(",\n");
            indent(sb, profLevel).append("\"name\": \"").append(p.getName()).append("\",\n");
            indent(sb, profLevel).append("\"formacao\": \"").append(p.getFormacao()).append("\"\n");
            indent(sb, innerLevel).append("},\n");

            indent(sb, innerLevel).append("\"students\": [\n");
            int studLevel = innerLevel + 1;
            boolean firstStudent = true;
            for (Student s : c.getEnrolledStudents()) {
                if (!firstStudent) {
                    sb.append(",\n");
                }
                indent(sb, studLevel).append("{\"id\": ").append(s.getId())
                        .append(", \"name\": \"").append(s.getName()).append("\"}");
                firstStudent = false;
            }
            if (!firstStudent) {
                sb.append("\n");
            }
            indent(sb, innerLevel).append("]\n");

            indent(sb, level).append("}");
            first = false;
        }

        sb.append("\n]");
        return sb.toString();
    }

    private StringBuilder indent(StringBuilder sb, int level) {
        sb.append(INDENT.repeat(Math.max(0, level)));
        return sb;
    }
}
