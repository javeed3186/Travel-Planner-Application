package travelplanner.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;
    private JCheckBox chkShowPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new RegisterUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RegisterUI() {
        setTitle("Travel Planner - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 430);
        setResizable(false);
        setLocationRelativeTo(null);

        // ---------- Background panel ----------
        Image bgImage = null;
        try {
            // same image as LoginUI: src/images/background.png
            bgImage = new ImageIcon(
                    getClass().getResource("/images/login_bg.png")
            ).getImage();
        } catch (Exception e) {
            System.out.println("Background image not found, starting without it.");
        }

        JPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new GridBagLayout());   // center the card
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
        bgPanel.add(card);

        // ----- Title -----
        JLabel lblTitle = new JLabel("Create Account", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);

        JLabel lblSub = new JLabel("Register to use Travel Planner", SwingConstants.CENTER);
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSub.setForeground(new Color(90, 90, 90));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSub);

        card.add(Box.createVerticalStrut(15));

        // ----- Form (3 rows x 2 columns) -----
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 12));
        formPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        JLabel lblConfirm  = new JLabel("Confirm Password:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtConfirm  = new JPasswordField(15);
        txtPassword.setEchoChar('•');
        txtConfirm.setEchoChar('•');

        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirm);
        formPanel.add(txtConfirm);

        card.add(formPanel);

        card.add(Box.createVerticalStrut(10));

        // ----- Show password checkbox -----
        chkShowPassword = new JCheckBox("Show passwords");
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
        chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(chkShowPassword);

        chkShowPassword.addActionListener(e -> {
            char echo = chkShowPassword.isSelected() ? 0 : '•';
            txtPassword.setEchoChar(echo);
            txtConfirm.setEchoChar(echo);
        });

        card.add(Box.createVerticalStrut(20));

        // ----- Buttons row -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnRegister = new JButton("Register");
        JButton btnBack    = new JButton("Back to Login");
        JButton btnExit    = new JButton("Exit");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnExit);

        card.add(buttonPanel);

        // ---------- Actions ----------
        btnRegister.addActionListener(this::handleRegister);

        btnBack.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    // ============= REGISTER LOGIC ============
    private void handleRegister(ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm  = new String(txtConfirm.getPassword());

        // Basic checks
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Only letters, numbers, underscore
        if (!username.matches("[A-Za-z0-9_]+")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username can contain only letters, numbers and underscore (no spaces).",
                    "Invalid Username",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password and Confirm Password do not match.",
                    "Password Mismatch",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password should be at least 4 characters.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File userFile = new File(dataDir, "users.txt");

        // Check if username already exists
        if (userFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.contains("|") ? line.split("\\|") : line.split(",");
                    if (parts.length < 2) continue;

                    String fileUser = parts[0].trim();
                    if (username.equals(fileUser)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Username is already taken. Please choose another.",
                                "Duplicate Username",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error while checking existing users.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        // Append new user
        try (FileWriter fw = new FileWriter(userFile, true)) {
            fw.write(username + "|" + password + System.lineSeparator());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error while saving user.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Registration successful! You can now log in."
        );

        new LoginUI().setVisible(true);
        dispose();
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
