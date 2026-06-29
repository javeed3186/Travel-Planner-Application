package travelplanner.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import travelplanner.model.Trip;

public class DayPlanUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private Image bgImage;
    private JPanel contentPane;

    private JTable table;
    private DefaultTableModel dayTableModel;

    private JLabel lblTripName;
    private JLabel lblCurrencyInfo;
    private JLabel lblTripTotals;
    private JLabel lblHomeTotals;

    private Trip trip;

    private HashMap<Integer, String> dayToDateMap = new HashMap<>();

    // Per-user file
    private String currentUser;
    private File dayPlanFile;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new DayPlanUI("DemoUser").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Base constructor (layout only)
    public DayPlanUI(String currentUser) {
        this.currentUser = currentUser;

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        String safeName = (currentUser == null || currentUser.trim().isEmpty())
                ? "default"
                : currentUser.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        this.dayPlanFile = new File(dataDir, "dayplans_" + safeName + ".txt");

        setTitle("Travel Planner - Day-wise Itinerary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        bgImage = new ImageIcon(getClass().getResource("/images/login_bg.png")).getImage();

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

        JLabel lblTitle = new JLabel("Day Plan for Trip");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 0, 0));
        lblTitle.setBounds(280, 10, 250, 30);
        contentPane.add(lblTitle);

        JPanel panelCard = new JPanel();
        panelCard.setLayout(null);
        panelCard.setBackground(new Color(255, 255, 255, 235));
        panelCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        panelCard.setBounds(40, 50, 660, 380);
        contentPane.add(panelCard);

        JLabel lblTripText = new JLabel("Trip:");
        lblTripText.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTripText.setBounds(10, 10, 40, 20);
        panelCard.add(lblTripText);

        lblTripName = new JLabel("<Trip Name>");
        lblTripName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblTripName.setBounds(55, 10, 250, 20);
        panelCard.add(lblTripName);

        lblCurrencyInfo = new JLabel("Trip: -  |  Home: -");
        lblCurrencyInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCurrencyInfo.setBounds(320, 10, 320, 20);
        panelCard.add(lblCurrencyInfo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 40, 635, 230);
        panelCard.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // UPDATED: Added "Time" column
        dayTableModel = new DefaultTableModel(
                new Object[] { "Day", "Date", "Time", "Category", "Activity / Place", "Cost" },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(dayTableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        lblTripTotals = new JLabel("Trip Currency: Total: 0.0 / Budget: 0.0 (Remaining: 0.0)");
        lblTripTotals.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblTripTotals.setBounds(10, 275, 635, 18);
        panelCard.add(lblTripTotals);

        lblHomeTotals = new JLabel("Home Currency: Total: 0.0 / Budget: 0.0 (Remaining: 0.0)");
        lblHomeTotals.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblHomeTotals.setBounds(10, 295, 635, 18);
        panelCard.add(lblHomeTotals);

        JButton btnAddDay = new JButton("Add Activity");
        btnAddDay.setBounds(40, 325, 130, 35);
        panelCard.add(btnAddDay);

        JButton btnEditDay = new JButton("Edit Activity");
        btnEditDay.setBounds(190, 325, 130, 35);
        panelCard.add(btnEditDay);

        JButton btnDeleteDay = new JButton("Delete Activity");
        btnDeleteDay.setBounds(340, 325, 140, 35);
        panelCard.add(btnDeleteDay);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(500, 325, 80, 35);
        panelCard.add(btnBack);

        // ===== Actions =====

        btnBack.addActionListener((ActionEvent e) -> {
            saveDayPlanToFile();
            dispose();
        });

        btnAddDay.addActionListener((ActionEvent e) -> {
            // Step 1: Get day number
            String dayText = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Enter Day Number (1, 2, 3...):"
            );
            if (dayText == null) return;

            int dayNumber;
            try {
                dayNumber = Integer.parseInt(dayText.trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DayPlanUI.this, "Invalid day number.");
                return;
            }

            // Step 2: Get or reuse date
            String date;
            if (dayToDateMap.containsKey(dayNumber)) {
                date = dayToDateMap.get(dayNumber);
            } else {
                date = JOptionPane.showInputDialog(
                        DayPlanUI.this,
                        "Enter Date for Day " + dayNumber + " (YYYY-MM-DD):"
                );
                if (date == null || date.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(DayPlanUI.this, "Date cannot be empty.");
                    return;
                }
                date = date.trim();
                dayToDateMap.put(dayNumber, date);
            }

            // Step 3: Get time (NEW!)
            String time = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Enter time (e.g., 10:00 AM, 14:30):"
            );
            if (time == null || time.trim().isEmpty()) {
                time = "Anytime";  // Default if user skips
            }
            time = time.trim();

            // Step 4: Select category
            String[] categories = { "Restaurant", "Shopping", "Nature", "Museum", "Other" };
            String category = (String) JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Select Category:",
                    "Activity Category",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    categories,
                    "Other"
            );
            if (category == null) return;

            // Step 5: Get activity/place
            String notes = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Enter activity / place:"
            );
            if (notes == null) return;
            notes = notes.trim();

            // Step 6: Get cost
            String costText = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Enter cost (number):"
            );
            if (costText == null) return;

            double cost;
            try {
                cost = Double.parseDouble(costText.trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DayPlanUI.this, "Cost must be a number.");
                return;
            }

            // UPDATED: Added time to table row
            dayTableModel.addRow(new Object[] {
                    dayNumber, date, time, category, notes, cost
            });

            updateTotals();
        });

        btnEditDay.addActionListener((ActionEvent e) -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(DayPlanUI.this, "Please select a row to edit.");
                return;
            }

            // UPDATED: Get values with new column indices
            int dayNumber = Integer.parseInt(dayTableModel.getValueAt(row, 0).toString());
            String date = dayTableModel.getValueAt(row, 1).toString();
            String currentTime = dayTableModel.getValueAt(row, 2).toString();
            String currentCategory = dayTableModel.getValueAt(row, 3).toString();
            String currentNotes = dayTableModel.getValueAt(row, 4).toString();
            String currentCostStr = dayTableModel.getValueAt(row, 5).toString();

            // Edit time (NEW!)
            String newTime = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Edit time:",
                    currentTime
            );
            if (newTime == null) return;

            // Edit category
            String[] categories = { "Restaurant", "Shopping", "Nature", "Museum", "Other" };
            String newCategory = (String) JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Edit category:",
                    "Edit Activity (Day " + dayNumber + " - " + date + ")",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    categories,
                    currentCategory
            );
            if (newCategory == null) return;

            // Edit notes
            String newNotes = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Edit activity / place:",
                    currentNotes
            );
            if (newNotes == null) return;

            // Edit cost
            String newCostText = JOptionPane.showInputDialog(
                    DayPlanUI.this,
                    "Edit cost:",
                    currentCostStr
            );
            if (newCostText == null) return;

            double newCost;
            try {
                newCost = Double.parseDouble(newCostText.trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DayPlanUI.this, "Cost must be a number.");
                return;
            }

            // UPDATED: Set values with new column indices
            dayTableModel.setValueAt(newTime, row, 2);
            dayTableModel.setValueAt(newCategory, row, 3);
            dayTableModel.setValueAt(newNotes, row, 4);
            dayTableModel.setValueAt(newCost, row, 5);

            updateTotals();
        });

        btnDeleteDay.addActionListener((ActionEvent e) -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(DayPlanUI.this, "Please select a row to delete.");
                return;
            }

            dayTableModel.removeRow(row);
            updateTotals();
        });
    }

    // Constructor used from ViewTripsUI
    /**
     * @wbp.parser.constructor
     */
    public DayPlanUI(String currentUser, Trip trip) {
        this(currentUser);   // Build layout
        this.trip = trip;

        if (trip != null) {
            lblTripName.setText(trip.getTripName());

            String tripCode  = trip.getCurrencyCode();
            String tripSym   = trip.getCurrencySymbol();
            String homeCode  = trip.getHomeCurrencyCode();
            String homeSym   = trip.getHomeCurrencySymbol();

            lblCurrencyInfo.setText(String.format(
                    "Trip: %s (%s)  |  Home: %s (%s)",
                    tripCode, tripSym, homeCode, homeSym
            ));

            // UPDATED: Cost column is now index 5
            table.getColumnModel().getColumn(5).setHeaderValue("Cost (" + tripSym + ")");
            table.getTableHeader().repaint();

            loadDayPlanFromFile();
            updateTotals();
        }
    }

    // ===== File I/O =====

    private void loadDayPlanFromFile() {
        dayTableModel.setRowCount(0);
        dayToDateMap.clear();

        if (trip == null) return;
        if (!dayPlanFile.exists()) return;

        try (Scanner scanner = new Scanner(dayPlanFile, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // UPDATED: TripName|Day|Date|Time|Category|Notes|Cost (7 fields)
                String[] parts = line.split("\\|");
                if (parts.length < 7) continue;

                String tripName = parts[0];
                if (!tripName.equals(trip.getTripName())) continue;

                int day = Integer.parseInt(parts[1]);
                String date = parts[2];
                String time = parts[3];        // NEW!
                String category = parts[4];
                String notes = parts[5];
                double cost = Double.parseDouble(parts[6]);

                // UPDATED: Added time to row
                dayTableModel.addRow(new Object[] { day, date, time, category, notes, cost });

                if (!dayToDateMap.containsKey(day)) {
                    dayToDateMap.put(day, date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDayPlanToFile() {
        if (trip == null) return;

        try {
            if (!dayPlanFile.getParentFile().exists()) {
                dayPlanFile.getParentFile().mkdirs();
            }

            List<String> allLines = new ArrayList<>();

            // Keep activities from other trips
            if (dayPlanFile.exists()) {
                try (Scanner scanner = new Scanner(dayPlanFile, "UTF-8")) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.trim().isEmpty()) continue;

                        String[] parts = line.split("\\|");
                        if (parts.length < 1) continue;

                        String tripName = parts[0];
                        if (!tripName.equals(trip.getTripName())) {
                            allLines.add(line);
                        }
                    }
                }
            }

            // Add current trip's activities
            for (int i = 0; i < dayTableModel.getRowCount(); i++) {
                Object day = dayTableModel.getValueAt(i, 0);
                Object date = dayTableModel.getValueAt(i, 1);
                Object time = dayTableModel.getValueAt(i, 2);     // NEW!
                Object category = dayTableModel.getValueAt(i, 3);
                Object notes = dayTableModel.getValueAt(i, 4);
                Object cost = dayTableModel.getValueAt(i, 5);

                // UPDATED: Added time to file format
                String line = trip.getTripName() + "|" +
                              day.toString() + "|" +
                              date.toString() + "|" +
                              time.toString() + "|" +
                              category.toString() + "|" +
                              notes.toString() + "|" +
                              cost.toString();

                allLines.add(line);
            }

            // Write all back to file
            try (FileWriter writer = new FileWriter(dayPlanFile, false)) {
                for (String l : allLines) {
                    writer.write(l + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== Totals (trip + home currencies) =====

    private void updateTotals() {
        if (trip == null) {
            lblTripTotals.setText("Trip Currency: -");
            lblHomeTotals.setText("Home Currency: -");
            return;
        }

        double totalCost = 0.0;
        for (int i = 0; i < dayTableModel.getRowCount(); i++) {
            // UPDATED: Cost column is now index 5
            Object value = dayTableModel.getValueAt(i, 5);
            try {
                totalCost += Double.parseDouble(value.toString());
            } catch (Exception ex) {
                // ignore
            }
        }

        double budget = trip.getBudget();
        double remaining = budget - totalCost;

        String tripCode = trip.getCurrencyCode();
        String tripSym  = trip.getCurrencySymbol();

        lblTripTotals.setText(String.format(
                "Trip Currency (%s): Total: %.1f %s / Budget: %.1f %s (Remaining: %.1f %s)",
                tripCode, totalCost, tripSym, budget, tripSym, remaining, tripSym
        ));

        double tripRate = trip.getRatePerINR();
        double homeRate = trip.getHomeRatePerINR();
        if (tripRate <= 0) tripRate = 1.0;
        if (homeRate <= 0) homeRate = 1.0;

        double totalINR     = totalCost / tripRate;
        double budgetINR    = budget / tripRate;
        double remainingINR = remaining / tripRate;

        double totalHome     = totalINR * homeRate;
        double budgetHome    = budgetINR * homeRate;
        double remainingHome = remainingINR * homeRate;

        String homeCode = trip.getHomeCurrencyCode();
        String homeSym  = trip.getHomeCurrencySymbol();

        lblHomeTotals.setText(String.format(
                "Home Currency (%s): Total: %.1f %s / Budget: %.1f %s (Remaining: %.1f %s)",
                homeCode, totalHome, homeSym, budgetHome, homeSym, remainingHome, homeSym
        ));
    }
}