package ui;

import entidades.Course;
import entidades.Professor;
import entidades.Student;
import manager.CourseManager;
import observer.Observer;
import util.CSVReportStrategy;
import util.HTMLReportStrategy;
import util.JSONReportStrategy;
import util.ReportStrategy;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame implements Observer {
    private final CourseManager manager = CourseManager.getInstance();
    private final JTextArea reportArea;
    private final JComboBox<String> reportFormatCombo;

    // Painel Curso
    private final JTextField courseIdField;
    private final JTextField courseNameField;
    private final JTextField professorSearchField;
    private final DefaultListModel<Professor> professorListModel;
    private final JList<Professor> professorList;

    // Painel Professor
    private final JTextField profIdField;
    private final JTextField profNameField;
    private final JTextField profFormacaoField;

    // Painel Estudante
    private final JTextField studentIdField;
    private final JTextField studentNameField;
    private final JTextField studentCourseSearchField;
    private final JList<Course> studentCourseList;
    private final DefaultListModel<Course> studentCourseListModel;

    // Painel Matrícula
    private final JTextField enrollStudentSearchField;
    private final JTextField enrollCourseSearchField;
    private final DefaultListModel<Student> enrollStudentListModel;
    private final JList<Student> enrollStudentList;
    private final DefaultListModel<Course> enrollCourseListModel;
    private final JList<Course> enrollCourseList;

    public MainWindow() {
        super("Sistema de Gerenciamento de Cursos");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        manager.registerObserver(this);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Painel Professor
        JPanel professorPanel = new JPanel(new GridLayout(5, 2));
        professorPanel.add(new JLabel("Professor ID:"));
        profIdField = new JTextField();
        professorPanel.add(profIdField);
        professorPanel.add(new JLabel("Nome:"));
        profNameField = new JTextField();
        professorPanel.add(profNameField);
        professorPanel.add(new JLabel("Formação:"));
        profFormacaoField = new JTextField();
        professorPanel.add(profFormacaoField);
        JButton addProfessorButton = new JButton("Adicionar Professor");
        professorPanel.add(addProfessorButton);
        tabbedPane.addTab("Professor", professorPanel);

        // Painel Curso (com busca por professor)
        JPanel coursePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCourse = new GridBagConstraints();
        gbcCourse.insets = new Insets(5, 5, 5, 5);
        gbcCourse.fill = GridBagConstraints.HORIZONTAL;

        gbcCourse.gridx = 0;
        gbcCourse.gridy = 0;
        coursePanel.add(new JLabel("Course ID:"), gbcCourse);
        gbcCourse.gridx = 1;
        courseIdField = new JTextField();
        coursePanel.add(courseIdField, gbcCourse);

        gbcCourse.gridx = 0;
        gbcCourse.gridy = 1;
        coursePanel.add(new JLabel("Nome:"), gbcCourse);
        gbcCourse.gridx = 1;
        courseNameField = new JTextField();
        coursePanel.add(courseNameField, gbcCourse);

        gbcCourse.gridx = 0;
        gbcCourse.gridy = 2;
        coursePanel.add(new JLabel("Buscar Professor:"), gbcCourse);
        gbcCourse.gridx = 1;
        professorSearchField = new JTextField();
        coursePanel.add(professorSearchField, gbcCourse);

        gbcCourse.gridx = 0;
        gbcCourse.gridy = 3;
        coursePanel.add(new JLabel("Selecione Professor:"), gbcCourse);
        gbcCourse.gridx = 1;
        professorListModel = new DefaultListModel<>();
        professorList = new JList<>(professorListModel);
        professorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane profScroll = new JScrollPane(professorList);
        profScroll.setPreferredSize(new Dimension(200, 150));
        coursePanel.add(profScroll, gbcCourse);

        gbcCourse.gridx = 0;
        gbcCourse.gridy = 4;
        gbcCourse.gridwidth = 2;
        JButton addCourseButton = new JButton("Adicionar Curso");
        coursePanel.add(addCourseButton, gbcCourse);
        gbcCourse.gridwidth = 1;

        tabbedPane.addTab("Curso", coursePanel);

        // Painel de Estudante (com busca de curso)
        JPanel studentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcStudent = new GridBagConstraints();
        gbcStudent.insets = new Insets(5, 5, 5, 5);
        gbcStudent.fill = GridBagConstraints.HORIZONTAL;

        gbcStudent.gridx = 0;
        gbcStudent.gridy = 0;
        studentPanel.add(new JLabel("Student ID:"), gbcStudent);
        gbcStudent.gridx = 1;
        studentIdField = new JTextField();
        studentPanel.add(studentIdField, gbcStudent);

        gbcStudent.gridx = 0;
        gbcStudent.gridy = 1;
        studentPanel.add(new JLabel("Nome:"), gbcStudent);
        gbcStudent.gridx = 1;
        studentNameField = new JTextField();
        studentPanel.add(studentNameField, gbcStudent);

        gbcStudent.gridx = 0;
        gbcStudent.gridy = 2;
        studentPanel.add(new JLabel("Buscar Cursos:"), gbcStudent);
        gbcStudent.gridx = 1;
        studentCourseSearchField = new JTextField();
        studentPanel.add(studentCourseSearchField, gbcStudent);

        gbcStudent.gridx = 0;
        gbcStudent.gridy = 3;
        studentPanel.add(new JLabel("Cursos para matrícula:"), gbcStudent);
        gbcStudent.gridx = 1;
        studentCourseListModel = new DefaultListModel<>();
        studentCourseList = new JList<>(studentCourseListModel);
        studentCourseList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane courseScroll = new JScrollPane(studentCourseList);
        courseScroll.setPreferredSize(new Dimension(200, 150));
        studentPanel.add(courseScroll, gbcStudent);

        gbcStudent.gridx = 0;
        gbcStudent.gridy = 4;
        gbcStudent.gridwidth = 2;
        JButton addStudentButton = new JButton("Adicionar Estudante");
        studentPanel.add(addStudentButton, gbcStudent);
        gbcStudent.gridwidth = 1;

        tabbedPane.addTab("Estudante", studentPanel);

        // Painel de Matrícula (com busca para aluno e curso)
        JPanel enrollPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcEnroll = new GridBagConstraints();
        gbcEnroll.insets = new Insets(5, 5, 5, 5);
        gbcEnroll.fill = GridBagConstraints.HORIZONTAL;

        gbcEnroll.gridx = 0;
        gbcEnroll.gridy = 0;
        enrollPanel.add(new JLabel("Buscar Aluno:"), gbcEnroll);
        gbcEnroll.gridx = 1;
        enrollStudentSearchField = new JTextField();
        enrollPanel.add(enrollStudentSearchField, gbcEnroll);

        gbcEnroll.gridx = 0;
        gbcEnroll.gridy = 1;
        enrollPanel.add(new JLabel("Selecione Aluno:"), gbcEnroll);
        gbcEnroll.gridx = 1;
        enrollStudentListModel = new DefaultListModel<>();
        enrollStudentList = new JList<>(enrollStudentListModel);
        enrollStudentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane studScroll = new JScrollPane(enrollStudentList);
        studScroll.setPreferredSize(new Dimension(200, 100));
        enrollPanel.add(studScroll, gbcEnroll);

        gbcEnroll.gridx = 0;
        gbcEnroll.gridy = 2;
        enrollPanel.add(new JLabel("Buscar Curso:"), gbcEnroll);
        gbcEnroll.gridx = 1;
        enrollCourseSearchField = new JTextField();
        enrollPanel.add(enrollCourseSearchField, gbcEnroll);

        gbcEnroll.gridx = 0;
        gbcEnroll.gridy = 3;
        enrollPanel.add(new JLabel("Selecione Curso:"), gbcEnroll);
        gbcEnroll.gridx = 1;
        enrollCourseListModel = new DefaultListModel<>();
        enrollCourseList = new JList<>(enrollCourseListModel);
        enrollCourseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane ecScroll = new JScrollPane(enrollCourseList);
        ecScroll.setPreferredSize(new Dimension(200, 100));
        enrollPanel.add(ecScroll, gbcEnroll);

        gbcEnroll.gridx = 0;
        gbcEnroll.gridy = 4;
        gbcEnroll.gridwidth = 2;
        JButton enrollButton = new JButton("Matricular Aluno no Curso");
        enrollPanel.add(enrollButton, gbcEnroll);
        gbcEnroll.gridwidth = 1;

        tabbedPane.addTab("Matrícula", enrollPanel);

        add(tabbedPane, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new BorderLayout());
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(reportArea);
        reportPanel.add(scroll, BorderLayout.CENTER);

        JPanel reportControlPanel = new JPanel(new FlowLayout());
        reportControlPanel.add(new JLabel("Formato do Relatório:"));
        reportFormatCombo = new JComboBox<>(new String[]{"JSON", "TABELA", "HTML"});
        reportControlPanel.add(reportFormatCombo);
        JButton generateReportButton = new JButton("Gerar Relatório");
        reportControlPanel.add(generateReportButton);

        JButton showAllDataJsonButton = new JButton("Listar Todos Dados (JSON)");
        reportControlPanel.add(showAllDataJsonButton);

        add(reportPanel, BorderLayout.CENTER);
        add(reportControlPanel, BorderLayout.SOUTH);

        // Listeners de adicionar
        addProfessorButton.addActionListener(e -> {
            String pidStr = profIdField.getText().trim();
            String pname = profNameField.getText().trim();
            String pform = profFormacaoField.getText().trim();
            if (!pidStr.isEmpty() && !pname.isEmpty() && !pform.isEmpty()) {
                try {
                    int pid = Integer.parseInt(pidStr);
                    manager.addProfessor(pid, pname, pform);
                    profIdField.setText("");
                    profNameField.setText("");
                    profFormacaoField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID do professor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos do professor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        addCourseButton.addActionListener(e -> {
            String cidStr = courseIdField.getText().trim();
            String cname = courseNameField.getText().trim();
            Professor selectedProfessor = professorList.getSelectedValue();
            if (!cidStr.isEmpty() && !cname.isEmpty() && selectedProfessor != null) {
                try {
                    int cid = Integer.parseInt(cidStr);
                    manager.createCourse(cid, cname, selectedProfessor);
                    courseIdField.setText("");
                    courseNameField.setText("");
                    professorList.clearSelection();
                    professorSearchField.setText("");
                    filterProfessors("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "IDs devem ser valores numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos do curso e selecione um professor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        addStudentButton.addActionListener(e -> {
            String sidStr = studentIdField.getText().trim();
            String sname = studentNameField.getText().trim();
            if (!sidStr.isEmpty() && !sname.isEmpty()) {
                try {
                    int sid = Integer.parseInt(sidStr);
                    manager.addStudent(sid, sname);
                    for (Course selectedCourse : studentCourseList.getSelectedValuesList()) {
                        manager.enrollStudentInCourse(selectedCourse.getCourseId(), sid);
                    }
                    studentIdField.setText("");
                    studentNameField.setText("");
                    studentCourseList.clearSelection();
                    studentCourseSearchField.setText("");
                    filterCoursesForStudent("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID do aluno inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos do aluno.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        enrollButton.addActionListener(e -> {
            Student selectedStudent = enrollStudentList.getSelectedValue();
            Course selectedCourse = enrollCourseList.getSelectedValue();
            if (selectedStudent != null && selectedCourse != null) {
                manager.enrollStudentInCourse(selectedCourse.getCourseId(), selectedStudent.getId());
                enrollStudentList.clearSelection();
                enrollCourseList.clearSelection();
                enrollStudentSearchField.setText("");
                enrollCourseSearchField.setText("");
                filterStudentsForEnroll("");
                filterCoursesForEnroll("");
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um aluno e um curso.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        generateReportButton.addActionListener(e -> generateReport());
        showAllDataJsonButton.addActionListener(e -> {
            String allDataJson = manager.getAllDataAsJson();
            reportArea.setText(allDataJson);
        });

        // Listeners de pesquisa
        professorSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProfessors(professorSearchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProfessors(professorSearchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterProfessors(professorSearchField.getText().trim());
            }
        });

        studentCourseSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterCoursesForStudent(studentCourseSearchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterCoursesForStudent(studentCourseSearchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterCoursesForStudent(studentCourseSearchField.getText().trim());
            }
        });

        enrollStudentSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterStudentsForEnroll(enrollStudentSearchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterStudentsForEnroll(enrollStudentSearchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterStudentsForEnroll(enrollStudentSearchField.getText().trim());
            }
        });

        enrollCourseSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterCoursesForEnroll(enrollCourseSearchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterCoursesForEnroll(enrollCourseSearchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterCoursesForEnroll(enrollCourseSearchField.getText().trim());
            }
        });

        enrollStudentList.addListSelectionListener(e -> filterCoursesForEnroll(enrollCourseSearchField.getText().trim()));

        enrollCourseList.addListSelectionListener(e -> filterStudentsForEnroll(enrollStudentSearchField.getText().trim()));

        updateAllLists();
    }

    private void generateReport() {
        String format = (String) reportFormatCombo.getSelectedItem();
        ReportStrategy strategy = switch (format) {
            case "CSV" -> new CSVReportStrategy();
            case "HTML" -> new HTMLReportStrategy();
            default -> new JSONReportStrategy();
        };
        String report = strategy.generateReport(manager.getCourseCollection());
        reportArea.setText(report);
    }

    @Override
    public void update() {
        setTitle("Sistema de Gerenciamento de Cursos - Dados Atualizados");
        updateAllLists();
    }

    private void updateAllLists() {
        updateProfessorList();
        updateCourseListForStudent();
        updateStudentListForEnroll();
        updateCourseListForEnroll();
    }

    private void updateProfessorList() {
        professorListModel.clear();
        for (Professor p : manager.getProfessors()) {
            professorListModel.addElement(p);
        }
    }

    private void updateCourseListForStudent() {
        studentCourseListModel.clear();
        for (Course c : manager.getCourseCollection().getAllCourses()) {
            studentCourseListModel.addElement(c);
        }
    }

    private void updateStudentListForEnroll() {
        enrollStudentListModel.clear();
        for (Student s : manager.getStudents()) {
            enrollStudentListModel.addElement(s);
        }
    }

    private void updateCourseListForEnroll() {
        enrollCourseListModel.clear();
        for (Course c : manager.getCourseCollection().getAllCourses()) {
            enrollCourseListModel.addElement(c);
        }
    }

    private void filterProfessors(String query) {
        professorListModel.clear();
        List<Professor> filtered = manager.getProfessors().stream()
                .filter(p -> p.toString().toLowerCase().contains(query.toLowerCase()))
                .toList();
        for (Professor p : filtered) professorListModel.addElement(p);
    }

    private void filterCoursesForStudent(String query) {
        studentCourseListModel.clear();
        List<Course> filtered = manager.getCourseCollection().getAllCourses().stream()
                .filter(c -> c.toString().toLowerCase().contains(query.toLowerCase()))
                .toList();
        for (Course c : filtered) studentCourseListModel.addElement(c);
    }

    private void filterStudentsForEnroll(String query) {
        enrollStudentListModel.clear();
        List<Student> filtered = manager.getStudents().stream()
                .filter(s -> s.toString().toLowerCase().contains(query.toLowerCase()))
                .toList();
        Course selectedCourse = enrollCourseList.getSelectedValue();
        if (selectedCourse != null) {
            filtered = filtered.stream()
                    .filter(s -> selectedCourse.getEnrolledStudents().stream().noneMatch(es -> es.getId() == s.getId()))
                    .toList();
        }

        for (Student st : filtered) enrollStudentListModel.addElement(st);
    }

    private void filterCoursesForEnroll(String query) {
        enrollCourseListModel.clear();
        List<Course> filtered = manager.getCourseCollection().getAllCourses().stream()
                .filter(c -> c.toString().toLowerCase().contains(query.toLowerCase()))
                .toList();

        Student selectedStudent = enrollStudentList.getSelectedValue();
        if (selectedStudent != null) {
            filtered = filtered.stream()
                    .filter(c -> c.getEnrolledStudents().stream().noneMatch(es -> es.getId() == selectedStudent.getId()))
                    .toList();
        }

        for (Course c : filtered) enrollCourseListModel.addElement(c);
    }
}
