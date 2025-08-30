import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReportPanel extends JPanel {
    private final UniversityService service;
    private final DefaultTableModel model;
    private final JComboBox<Course> courseBox;

    public ReportPanel(UniversityService service) {
        super(new BorderLayout());
        this.service = service;
        setBackground(new Color(245, 247, 250)); 

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        top.setBackground(new Color(245, 247, 250));

        JLabel lbl = new JLabel("Course:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        courseBox = new JComboBox<>(service.allCourses().toArray(new Course[0]));
        courseBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        courseBox.setPreferredSize(new Dimension(200, 28));

        JButton showBtn = styleButton("Show Students in Course", 
                                      new Color(52, 152, 219), 
                                      new Color(41, 128, 185));

        top.add(lbl);
        top.add(courseBox);
        top.add(showBtn);

        model = new DefaultTableModel(new Object[]{"Student ID","Name","Dept","GPA"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
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

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        showBtn.addActionListener(e -> loadReport());
    }

    public void refreshReports() {
        courseBox.removeAllItems();
        for (Course c : service.allCourses()) {
            courseBox.addItem(c);
        }
        model.setRowCount(0);
        if (courseBox.getItemCount() > 0) {
            courseBox.setSelectedIndex(0);
            loadReport();
        }
    }

    private void loadReport() {
        model.setRowCount(0);
        Course c = (Course) courseBox.getSelectedItem();
        if (c == null) return;
        for (Student s : service.studentsInCourse(c.getCourseId())) {
            double grade = service.gradeInCourse(s.getId(), c.getCourseId());
            model.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getDepartment(),
                String.format("%.2f", grade)
            });
        }
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
