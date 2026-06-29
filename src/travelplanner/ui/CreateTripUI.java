package travelplanner.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import travelplanner.manager.TripManager;
import travelplanner.model.Trip;

public class CreateTripUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    // UI fields
    private JTextField txtDestination;
    private JTextField txtBudget;
    private JComboBox<String> cmbCountry;

    // NEW: date pickers instead of plain text fields
    private JSpinner spStartDate;
    private JSpinner spEndDate;

    // Trip manager + current user
    private TripManager tripManager;
    private String currentUser;

    // Background image
    private Image bgImage;

    // Date formatter for saving as "YYYY-MM-DD"
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new CreateTripUI("DemoUser").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CreateTripUI(String currentUser) {
        this.currentUser = currentUser;
        this.tripManager = new TripManager(currentUser);

        setTitle("Travel Planner - Create Trip");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 520, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Load background image ====
        bgImage = new ImageIcon(getClass().getResource("/images/login_bg.png")).getImage();

        // ==== Custom background panel ====
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // ===== Title (top on background) =====
        JLabel lblTitle = new JLabel("Create New Trip");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 0, 0));
        lblTitle.setBounds(170, 15, 220, 30);
        contentPane.add(lblTitle);

        // ===== Card panel in center =====
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(70, 60, 370, 260);
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        contentPane.add(card);

        // ===== Destination =====
        JLabel lblDestination = new JLabel("Destination:");
        lblDestination.setBounds(30, 30, 120, 25);
        card.add(lblDestination);

        txtDestination = new JTextField();
        txtDestination.setBounds(150, 30, 190, 25);
        card.add(txtDestination);

        // ===== Home Country =====
        JLabel lblCountry = new JLabel("Home Country:");
        lblCountry.setBounds(30, 65, 120, 25);
        card.add(lblCountry);

        cmbCountry = new JComboBox<>(new String[] {
                "India",
                "South Korea",
                "Japan",
                "USA",
                "China",
                "Canada",
                "Switzerland",
                "France"
        });
        cmbCountry.setBounds(150, 65, 190, 25);
        card.add(cmbCountry);

        // ===== Start Date (with date picker) =====
        JLabel lblStartDate = new JLabel("Start Date:");
        lblStartDate.setBounds(30, 100, 120, 25);
        card.add(lblStartDate);

        spStartDate = new JSpinner(new SpinnerDateModel());
        spStartDate.setBounds(150, 100, 190, 25);
        JSpinner.DateEditor startEditor =
                new JSpinner.DateEditor(spStartDate, "yyyy-MM-dd");
        spStartDate.setEditor(startEditor);
        card.add(spStartDate);

        // ===== End Date (with date picker) =====
        JLabel lblEndDate = new JLabel("End Date:");
        lblEndDate.setBounds(30, 135, 120, 25);
        card.add(lblEndDate);

        spEndDate = new JSpinner(new SpinnerDateModel());
        spEndDate.setBounds(150, 135, 190, 25);
        JSpinner.DateEditor endEditor =
                new JSpinner.DateEditor(spEndDate, "yyyy-MM-dd");
        spEndDate.setEditor(endEditor);
        card.add(spEndDate);

        // ===== Budget =====
        JLabel lblBudget = new JLabel("Budget (Trip Currency):");
        lblBudget.setBounds(30, 170, 180, 25);
        card.add(lblBudget);

        txtBudget = new JTextField();
        txtBudget.setBounds(210, 170, 130, 25);
        card.add(txtBudget);

        // ===== Buttons =====
        JButton btnSave = new JButton("Save Trip");
        btnSave.setBounds(80, 210, 100, 30);
        card.add(btnSave);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(190, 210, 100, 30);
        card.add(btnBack);

        // ===== Button actions =====

        // Back → go to Dashboard, close this window
        btnBack.addActionListener(e -> {
            new DashboardUI(currentUser).setVisible(true);
            dispose();
        });

        // Save Trip
        btnSave.addActionListener(e -> handleSaveTrip());
    }

    // ===== Helper: get formatted date from spinner =====
    private String formatSpinnerDate(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return dateFormat.format(date);
    }

    // ===== Logic for saving trip =====
    private void handleSaveTrip() {
        String destination = txtDestination.getText().trim();
        String homeCountry = (String) cmbCountry.getSelectedItem();
        String budgetText  = txtBudget.getText().trim();

        // Read dates from the spinners
        Date start = (Date) spStartDate.getValue();
        Date end   = (Date) spEndDate.getValue();

        // Basic checks
        if (destination.isEmpty() || budgetText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in destination and budget.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (end.before(start)) {
            JOptionPane.showMessageDialog(
                    this,
                    "End date cannot be before start date.",
                    "Date Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Budget must be a number.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String startDate = dateFormat.format(start);
        String endDate   = dateFormat.format(end);

        String tripName = destination;

        // store with correct home country
        Trip trip = new Trip(tripName, startDate, endDate, budget, homeCountry);

        tripManager.addTrip(trip);
        tripManager.saveTripsToFile();

        JOptionPane.showMessageDialog(
                this,
                "Trip saved successfully for " + destination +
                " (Home country: " + homeCountry + ")!"
        );

        new DashboardUI(currentUser).setVisible(true);
        dispose();
    }
}
