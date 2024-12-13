package util;

import entidades.Course;
import entidades.Professor;
import entidades.Student;
import iterator.CourseCollection;
import iterator.CourseIterator;

import java.util.ArrayList;
import java.util.List;


public class CSVReportStrategy implements ReportStrategy {
    @Override
    public String generateReport(CourseCollection courses) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{
                "CourseID", "CourseName", "ProfessorID", "ProfessorName", "ProfessorFormacao", "Students"
        });

        CourseIterator it = courses.iterator();
        while (it.hasNext()) {
            Course c = it.next();
            Professor p = c.getProfessor();
            StringBuilder studentsList = new StringBuilder();
            for (Student s : c.getEnrolledStudents()) {
                studentsList.append(s.getId()).append(" ").append(s.getName()).append("; ");
            }
            rows.add(new String[]{
                    String.valueOf(c.getCourseId()),
                    c.getName() == null ? "" : c.getName(),
                    String.valueOf(p.getId()),
                    p.getName() == null ? "" : p.getName(),
                    p.getFormacao() == null ? "" : p.getFormacao(),
                    studentsList.toString().trim()
            });
        }

        if (rows.isEmpty()) {
            return "";
        }

        int colCount = rows.get(0).length;

        List<List<String>> columns = new ArrayList<>(colCount);
        for (int i = 0; i < colCount; i++) {
            columns.add(new ArrayList<>());
        }

        for (String[] row : rows) {
            for (int i = 0; i < colCount; i++) {
                columns.get(i).add(row[i] == null ? "" : row[i]);
            }
        }


        int[] maxWidths = new int[colCount];
        for (int i = 0; i < colCount; i++) {
            int max = 0;
            for (String val : columns.get(i)) {
                int len = val.length();
                if (len > max) {
                    max = len;
                }
            }
            maxWidths[i] = max;
        }

        for (int i = 0; i < colCount; i++) {
            List<String> col = columns.get(i);
            for (int r = 0; r < col.size(); r++) {
                col.set(r, padRight(col.get(r), maxWidths[i]));
            }
        }


        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for (int i = 0; i < colCount; i++) {
            sb.append(repeatChar('-', maxWidths[i] + 2)).append("+");
        }
        sb.append("\n");

        for (int r = 0; r < rows.size(); r++) {
            sb.append("|");
            for (int i = 0; i < colCount; i++) {
                sb.append(" ").append(columns.get(i).get(r)).append(" |");
            }
            sb.append("\n");
            if (r == 0) {
                sb.append("+");
                for (int i = 0; i < colCount; i++) {
                    sb.append(repeatChar('-', maxWidths[i] + 2)).append("+");
                }
                sb.append("\n");
            }
        }

        sb.append("+");
        for (int i = 0; i < colCount; i++) {
            sb.append(repeatChar('-', maxWidths[i] + 2)).append("+");
        }
        sb.append("\n");

        return sb.toString();
    }

    private String padRight(String text, int length) {
        if (text == null) text = "";
        if (text.length() >= length) return text;
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
