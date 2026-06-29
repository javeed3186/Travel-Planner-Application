package travelplanner.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class DashboardUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private String currentUser;   // ✅ who is logged in

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new DashboardUI("DemoUser").setVisible(true);   // for testing
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ✅ main constructor with username
    public DashboardUI(String currentUser) {
        this.currentUser = currentUser;

        setTitle("Travel Planner - Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 420);
        setLocationRelativeTo(null);  // center
        setResizable(false);

        // ===== Load background image =====
        Image bgImage = null;
        try {
            bgImage = new ImageIcon(
                    getClass().getResource("/images/login_bg.png")
            ).getImage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JPanel bgPanel;
        if (bgImage != null) {
            bgPanel = new BackgroundPanel(bgImage);
        } else {
            // fallback if image not found
            bgPanel = new JPanel();
            bgPanel.setBackground(new Color(245, 247, 250));
        }

        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // ===== Title =====
        JLabel lblAppTitle = new JLabel("Travel Planner");
        lblAppTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblAppTitle.setForeground(new Color(40, 40, 40));
        lblAppTitle.setBounds(215, 10, 220, 32);
        bgPanel.add(lblAppTitle);

        JLabel lblSubTitle = new JLabel("Plan, organize, and track your trips easily");
        lblSubTitle.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSubTitle.setForeground(new Color(70, 70, 70));
        lblSubTitle.setBounds(180, 40, 280, 20);
        bgPanel.add(lblSubTitle);

        JSeparator separatorTop = new JSeparator();
        separatorTop.setBounds(60, 65, 500, 2);
        bgPanel.add(separatorTop);

        // ===== Card in the middle =====
        JPanel panelCard = new JPanel();
        panelCard.setLayout(null);
        panelCard.setBounds(120, 90, 360, 230);
        panelCard.setOpaque(false);
        panelCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel panelCardBg = new JPanel();
        panelCardBg.setLayout(null);
        panelCardBg.setBounds(panelCard.getBounds());
        panelCardBg.setBackground(new Color(255, 255, 255, 230));
        bgPanel.add(panelCardBg);
        panelCardBg.add(panelCard);
        panelCard.setBounds(0, 0, 360, 230);

        JLabel lblWelcome = new JLabel("Welcome to your dashboard");
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblWelcome.setBounds(75, 5, 230, 30);
        panelCard.add(lblWelcome);

        JLabel lblHint = new JLabel("Choose what you want to do:");
        lblHint.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblHint.setBounds(95, 30, 200, 20);
        panelCard.add(lblHint);

        // ===== Buttons =====
        JButton btnCreateTrip = new JButton("Create New Trip");
        btnCreateTrip.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCreateTrip.setBounds(100, 70, 160, 35);
        panelCard.add(btnCreateTrip);

        JButton btnViewTrips = new JButton("View Saved Trips");
        btnViewTrips.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnViewTrips.setBounds(100, 115, 160, 35);
        panelCard.add(btnViewTrips);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLogout.setBounds(100, 160, 160, 32);
        panelCard.add(btnLogout);

        // ===== Actions =====
        btnCreateTrip.addActionListener((ActionEvent e) -> {
            new CreateTripUI(currentUser).setVisible(true);
            dispose();  // close dashboard
        });

        btnViewTrips.addActionListener((ActionEvent e) -> {
            new ViewTripsUI(currentUser).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener((ActionEvent e) -> {
            new LoginUI().setVisible(true);
            dispose();
        });
    }

    // ===== helper panel =====
    private static class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final Image image;

        public BackgroundPanel(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
