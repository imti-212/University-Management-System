import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UniversityService service;

    public LoginFrame(UniversityService service) {
        super("Metropolitan University — Login");
        this.service = service;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 320);
        setLocationRelativeTo(null);

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 15));

        build();
    }

    private void build() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250)); 
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon originalIcon = new ImageIcon("./MU_Logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(220, 70, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(logoIcon, SwingConstants.CENTER);

        gc.gridx = 0; 
        gc.gridy = 0; 
        gc.gridwidth = 2; 
        panel.add(logoLabel, gc);

        gc.gridwidth = 1;

        JLabel uLbl = new JLabel("Username:");
        uLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField uTxt = new JTextField("admin");
        uTxt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel pLbl = new JLabel("Password:");
        pLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPasswordField pTxt = new JPasswordField("admin");
        pTxt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(52, 152, 219));
            }
        });

        gc.gridx = 0; gc.gridy = 1; panel.add(uLbl, gc);
        gc.gridx = 1; panel.add(uTxt, gc);
        gc.gridx = 0; gc.gridy = 2; panel.add(pLbl, gc);
        gc.gridx = 1; panel.add(pTxt, gc);
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2; panel.add(loginBtn, gc);

        loginBtn.addActionListener(e -> {
            String u = uTxt.getText().trim();
            String p = new String(pTxt.getPassword());
            if (u.equals("admin") && p.equals("admin")) {
                dispose();
                new MainFrame(service).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(panel);
    }
}
