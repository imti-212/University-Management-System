import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final StudentPanel studentPanel;
    private final CoursePanel coursePanel;
    private final EnrollmentPanel enrollmentPanel;
    private final ReportPanel reportPanel;

    public MainFrame(UniversityService service) {
        super("Metropolitan University — Student Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        UIManager.put("TabbedPane.selected", new Color(52, 152, 219));  
        UIManager.put("TabbedPane.background", new Color(236, 240, 241)); 
        UIManager.put("TabbedPane.foreground", Color.DARK_GRAY);         
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);     
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 16)); 
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

        studentPanel = new StudentPanel(service);
        coursePanel = new CoursePanel(service);
        enrollmentPanel = new EnrollmentPanel(service, this);
        reportPanel = new ReportPanel(service);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabs.setBackground(new Color(236, 240, 241));
        tabs.setForeground(Color.DARK_GRAY);

        tabs.addTab("👨‍🎓 Students", studentPanel);
        tabs.addTab("📘 Courses", coursePanel);
        tabs.addTab("📝 Enrollments", enrollmentPanel);
        tabs.addTab("📊 Reports", reportPanel);

        tabs.addChangeListener(e -> {
            Component comp = tabs.getSelectedComponent();
            if (comp instanceof EnrollmentPanel) {
                enrollmentPanel.refreshTable();
            } else if (comp instanceof ReportPanel) {
                reportPanel.refreshReports();
            } else if (comp instanceof StudentPanel) {
                studentPanel.refresh();
            }
        });

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);

        JMenuBar mb = new JMenuBar();
        mb.setBackground(new Color(44, 62, 80));
        mb.setBorderPainted(false);

        JMenu file = new JMenu("File");
        file.setForeground(Color.WHITE);
        file.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JMenuItem save = new JMenuItem("💾 Save");
        save.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        save.addActionListener(e -> {
            try {
                service.saveAll();
                JOptionPane.showMessageDialog(this, "Saved successfully.");
                refreshAll(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Save failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem exit = new JMenuItem("❌ Exit");
        exit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        exit.addActionListener(e -> System.exit(0));

        file.add(save);
        file.add(exit);
        mb.add(file);
        setJMenuBar(mb);
    }

    public void refreshAll() {
        studentPanel.refresh();
        coursePanel.refresh();
        enrollmentPanel.refreshTable();
        reportPanel.refreshReports();
    }
}
