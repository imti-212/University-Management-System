import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class EnrollmentPanel extends JPanel {
    private final UniversityService service;
    private final DefaultTableModel model;
    private JTable table;
    private JComboBox<Student> studentBox;
    private JComboBox<Course> courseBox;

    public EnrollmentPanel(UniversityService service, MainFrame mainFrame) {
        super(new BorderLayout());
        this.service = service;
        setBackground(new Color(245, 247, 250));

        model = new DefaultTableModel(
                new Object[]{"Student ID","Student Name","Course ID","Course Name","Grade"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        studentBox = new JComboBox<>(service.allStudents().toArray(new Student[0]));
        studentBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentBox.setPreferredSize(new Dimension(180, 28));

        courseBox = new JComboBox<>(service.allCourses().toArray(new Course[0]));
        courseBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        courseBox.setPreferredSize(new Dimension(180, 28));

        JButton enrollBtn = styleButton("Enroll", new Color(52, 152, 219), new Color(41, 128, 185));
        JComboBox<Grade> gradeBox = new JComboBox<>(Grade.values());
        gradeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gradeBox.setPreferredSize(new Dimension(100, 28));

        JButton assignGradeBtn = styleButton("Assign Grade", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton deleteBtn = styleButton("Delete Enrollment", new Color(231, 76, 60), new Color(192, 57, 43));

        JPanel top = new JPanel(new GridLayout(2, 1, 8, 8));
        top.setBackground(new Color(245, 247, 250));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        row1.setBackground(new Color(245, 247, 250));
        row1.add(new JLabel("Student:"));
        row1.add(studentBox);
        row1.add(new JLabel("Course:"));
        row1.add(courseBox);
        row1.add(enrollBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        row2.setBackground(new Color(245, 247, 250));
        row2.add(new JLabel("Grade:"));
        row2.add(gradeBox);
        row2.add(assignGradeBtn);
        row2.add(deleteBtn);

        top.add(row1);
        top.add(row2);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();

        enrollBtn.addActionListener(e -> {
            Student s = (Student) studentBox.getSelectedItem();
            Course c = (Course) courseBox.getSelectedItem();
            if (s == null || c == null) {
                JOptionPane.showMessageDialog(this, "Need student and course");
                return;
            }
            service.enroll(s.getId(), c.getCourseId());
            refreshTable();
        });

        assignGradeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an enrollment row");
                return;
            }
            String sid = (String) model.getValueAt(row, 0);
            String cid = (String) model.getValueAt(row, 2);
            Grade g = (Grade) gradeBox.getSelectedItem();
            service.assignGrade(sid, cid, g);
            refreshTable();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an enrollment row to delete");
                return;
            }
            String sid = (String) model.getValueAt(row, 0);
            String cid = (String) model.getValueAt(row, 2);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this enrollment?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteEnrollment(sid, cid);
                refreshTable();
            }
        });
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (Enrollment e : service.getEnrollments()) {
            model.addRow(new Object[]{
                e.getStudent().getId(), e.getStudent().getName(),
                e.getCourse().getCourseId(), e.getCourse().getCourseName(),
                e.getGrade() == null ? "-" : e.getGrade().toString()
            });
        }
        studentBox.setModel(new DefaultComboBoxModel<>(service.allStudents().toArray(new Student[0])));
        courseBox.setModel(new DefaultComboBoxModel<>(service.allCourses().toArray(new Course[0])));
    }

    private JButton styleButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}
