package travelplanner.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import travelplanner.manager.TripManager;
import travelplanner.model.Trip;

public class ViewTripsUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable table;
    private DefaultTableModel tableModel;

    private TripManager tripManager;
    private String currentUser;

    private Image bgImage;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new ViewTripsUI("DemoUser").setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ViewTripsUI(String currentUser) {
        this.currentUser = currentUser;
        this.tripManager = new TripManager(currentUser);

        setTitle("Travel Planner - My Trips");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 650, 430);
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
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // ===== Top title =====
        JLabel lblTitle = new JLabel("My Saved Trips");
        lblTitle.setBackground(new Color(255, 255, 255));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 0, 0));
        lblTitle.setBounds(195, 8, 250, 30);
        contentPane.add(lblTitle);

        JLabel lblSub = new JLabel("Double-click a trip to open its day-wise itinerary");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblSub.setForeground(new Color(0, 0, 0));
        lblSub.setBounds(195, 40, 300, 20);
        contentPane.add(lblSub);

        JSeparator sepTop = new JSeparator();
        sepTop.setBounds(50, 65, 540, 2);
        contentPane.add(sepTop);

        // ===== Card panel for table =====
        JPanel panelTable = new JPanel();
        panelTable.setLayout(null);
        panelTable.setBackground(new Color(255, 255, 255, 235));
        panelTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        panelTable.setBounds(50, 80, 540, 230);
        contentPane.add(panelTable);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 520, 210);
        panelTable.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel(
                new Object[] { "Destination", "Start Date", "End Date", "Budget" },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        // Double-click → open DayPlanUI
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    Trip selectedTrip = tripManager.getTrips().get(row);
                    new DayPlanUI(currentUser, selectedTrip).setVisible(true);
                    // ViewTrips stays open
                }
            }
        });

        // ===== Buttons =====
        JButton btnDelete = new JButton("Delete Trip");
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDelete.setBounds(60, 330, 120, 35);
        contentPane.add(btnDelete);

        JButton btnMoveUp = new JButton("Move Up");
        btnMoveUp.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnMoveUp.setBounds(195, 330, 110, 35);
        contentPane.add(btnMoveUp);

        JButton btnMoveDown = new JButton("Move Down");
        btnMoveDown.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnMoveDown.setBounds(315, 330, 120, 35);
        contentPane.add(btnMoveDown);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.setBounds(455, 330, 90, 35);
        contentPane.add(btnBack);

        JButton btnEdit = new JButton("Edit Trip");
        btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnEdit.setBounds(500, 15, 100, 25);
        contentPane.add(btnEdit);

        // ===== Actions =====
        btnBack.addActionListener((ActionEvent e) -> {
            new DashboardUI(currentUser).setVisible(true);
            dispose();
        });

        btnEdit.addActionListener((ActionEvent e) -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "Please select a trip to edit."
                );
                return;
            }

            Trip selectedTrip = tripManager.getTrips().get(row);

            String newName = JOptionPane.showInputDialog(
                    ViewTripsUI.this,
                    "Edit destination name:",
                    selectedTrip.getTripName()
            );

            if (newName == null) {
                return;
            }

            newName = newName.trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "Destination cannot be empty."
                );
                return;
            }

            selectedTrip.setTripName(newName);
            tripManager.saveTripsToFile();
            loadTripsToTable();
            table.setRowSelectionInterval(row, row);
        });

        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "Please select a trip to delete."
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    ViewTripsUI.this,
                    "Are you sure you want to delete this trip?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            tripManager.getTrips().remove(selectedRow);
            tripManager.saveTripsToFile();
            loadTripsToTable();

            JOptionPane.showMessageDialog(
                    ViewTripsUI.this,
                    "Trip deleted successfully!"
            );
        });

        btnMoveUp.addActionListener((ActionEvent e) -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "Please select a trip to move."
                );
                return;
            }

            if (row == 0) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "This trip is already at the top."
                );
                return;
            }

            Trip current = tripManager.getTrips().get(row);
            Trip above = tripManager.getTrips().get(row - 1);

            tripManager.getTrips().set(row - 1, current);
            tripManager.getTrips().set(row, above);

            tripManager.saveTripsToFile();
            loadTripsToTable();
            table.setRowSelectionInterval(row - 1, row - 1);
        });

        btnMoveDown.addActionListener((ActionEvent e) -> {
            int row = table.getSelectedRow();
            int total = tripManager.getTrips().size();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "Please select a trip to move."
                );
                return;
            }

            if (row == total - 1) {
                JOptionPane.showMessageDialog(
                        ViewTripsUI.this,
                        "This trip is already at the bottom."
                );
                return;
            }

            Trip current = tripManager.getTrips().get(row);
            Trip below = tripManager.getTrips().get(row + 1);

            tripManager.getTrips().set(row + 1, current);
            tripManager.getTrips().set(row, below);

            tripManager.saveTripsToFile();
            loadTripsToTable();
            table.setRowSelectionInterval(row + 1, row + 1);
        });

        // Initial load
        loadTripsToTable();
    }

    private void loadTripsToTable() {
        tableModel.setRowCount(0);

        for (Trip t : tripManager.getTrips()) {
            tableModel.addRow(new Object[] {
                t.getTripName(),
                t.getStartDate(),
                t.getEndDate(),
                t.getBudget()
            });
        }
    }
}
