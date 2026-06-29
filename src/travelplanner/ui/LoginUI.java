package travelplanner.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new LoginUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginUI() {
        setTitle("Travel Planner - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);

        // ---------- Background panel ----------
        Image bgImage = null;
        try {
            bgImage = new ImageIcon(
                    getClass().getResource("/images/login_bg.png")
            ).getImage();
        } catch (Exception e) {
            System.out.println("Background image not found, starting without it.");
        }

        JPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new GridBagLayout()); // to center the card
        setContentPane(bgPanel);

        // ---------- Card panel ----------
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(20, 30, 20, 30)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        bgPanel.add(card); // GridBagLayout centers it

        // ----- Title -----
        JLabel lblAppTitle = new JLabel("Travel Planner", SwingConstants.CENTER);
        lblAppTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblAppTitle);

        JLabel lblSub = new JLabel("Login to your account", SwingConstants.CENTER);
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSub.setForeground(new Color(90, 90, 90));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSub);

        card.add(Box.createVerticalStrut(15));

        // ----- Form panel (labels + fields) -----
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtPassword.setEchoChar('•');

        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        card.add(formPanel);

        card.add(Box.createVerticalStrut(10));

        // ----- Show password -----
        chkShowPassword = new JCheckBox("Show password");
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
        chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(chkShowPassword);

        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        card.add(Box.createVerticalStrut(20));

        // ----- Button row -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnExit = new JButton("Exit");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);

        card.add(buttonPanel);

        // ---------- Actions ----------
        btnLogin.addActionListener(this::handleLogin);
        btnRegister.addActionListener(e -> {
            new RegisterUI().setVisible(true);
            dispose();
        });
        btnExit.addActionListener(e -> System.exit(0));
    }

    // ============= LOGIN LOGIC ============
    private void handleLogin(ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        File userFile = new File("data/users.txt");
        if (!userFile.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No users found. Please register first.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean ok = false;

        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.contains("|") ? line.split("\\|") : line.split(",");
                if (parts.length < 2) continue;

                String fileUser = parts[0].trim();
                String filePass = parts[1].trim();

                if (username.equals(fileUser) && password.equals(filePass)) {
                    ok = true;
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error while reading users file.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (ok) {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
            // ✅ pass username forward
            new DashboardUI(username).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // -------- Background panel class --------
    private static class BackgroundPanel extends JPanel {
        private final Image bg;

        public BackgroundPanel(Image bg) {
            this.bg = bg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
