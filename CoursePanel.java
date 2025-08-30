import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePanel extends JPanel {
    private final UniversityService service;
    private final DefaultTableModel model;
    private JTable table;

    public CoursePanel(UniversityService service) {
        super(new BorderLayout());
        this.service = service;

        setBackground(new Color(245, 247, 250));

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Credit", "Department"}, 0) {
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

        refresh();

        JPanel form = new JPanel(new BorderLayout(10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form.setBackground(new Color(245, 247, 250));

        JPanel inputRow = new JPanel(new GridLayout(1, 6, 8, 8));
        inputRow.setBackground(new Color(245, 247, 250));

        JLabel nameLbl = new JLabel("Name:");
        JLabel creditLbl = new JLabel("Credit:");
        JLabel deptLbl = new JLabel("Department:");
        styleLabel(nameLbl);
        styleLabel(creditLbl);
        styleLabel(deptLbl);

        JTextField name = styleTextField();
        JTextField credit = styleTextField("3");
        JTextField dept = styleTextField("SWE");

        inputRow.add(nameLbl);
        inputRow.add(name);
        inputRow.add(creditLbl);
        inputRow.add(credit);
        inputRow.add(deptLbl);
        inputRow.add(dept);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonRow.setBackground(new Color(245, 247, 250));

        JButton add = styleButton("Add Course", new Color(46, 204, 113), new Color(39, 174, 96));    // green
        JButton edit = styleButton("Update Selected", new Color(230, 126, 34), new Color(211, 84, 0)); // orange
        JButton delete = styleButton("Delete Selected", new Color(231, 76, 60), new Color(192, 57, 43)); // red

        buttonRow.add(add);
        buttonRow.add(edit);
        buttonRow.add(delete);

        form.add(inputRow, BorderLayout.CENTER);
        form.add(buttonRow, BorderLayout.SOUTH);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            try {
                String nm = name.getText().trim();
                int cr = Integer.parseInt(credit.getText().trim());
                String dp = dept.getText().trim();
                if (nm.isEmpty() || dp.isEmpty()) throw new IllegalArgumentException("Fill all fields");
                service.addCourse(nm, cr, dp);
                refresh();
                name.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course to update");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            try {
                String nm = name.getText().trim();
                int cr = Integer.parseInt(credit.getText().trim());
                String dp = dept.getText().trim();
                service.updateCourse(id, nm, cr, dp);
                refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course to delete");
                return;
            }
            String id = (String) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Deleting this course will remove ALL its enrollments. Continue?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteCourse(id);
                refresh();
            }
        });
    }

    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private JTextField styleTextField() {
        return styleTextField("");
    }

    private JTextField styleTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton styleButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    public void refresh() {
        model.setRowCount(0);
        for (Course c : service.allCourses()) {
            model.addRow(new Object[]{c.getCourseId(), c.getCourseName(), c.getCredit(), c.getDepartment()});
        }
    }
}
