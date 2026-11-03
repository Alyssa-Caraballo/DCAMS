package com.dcams.gui;

import com.dcams.logic.AttractionManager;
import com.dcams.model.AttractionRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MainFrame serves as GUI for the DCAMS project.
 * This will feature data tables, file loading controls, form inputs, and input validation.
 */
public class MainFrame extends JFrame {
    private AttractionManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    // Input fields for manual entry
    private JTextField txtID, txtName, txtHeight, txtCapacity;
    private JComboBox<String> cmbLocation;
    private JCheckBox chkActive;

    public MainFrame() {
        manager = new AttractionManager();

        setTitle("DCAMS - Disney Character/Attraction Management System");
        setSize(1000, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Setup
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(184, 215, 238));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        JLabel lblTitle = new JLabel("DCAMS - Disney Character/Attraction Management System");
        lblTitle.setForeground(new Color(40, 60, 80));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Center Section (Table + File Loading)
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        JPanel topControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topControlPanel.setBorder(BorderFactory.createTitledBorder("Step 1: View Data"));
        JButton btnLoadFile = new JButton("Load attractions.txt");
        JButton btnRefresh = new JButton("Refresh Table");
        topControlPanel.add(btnLoadFile);
        topControlPanel.add(btnRefresh);

        centerPanel.add(topControlPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Location", "Min Height (in)", "Daily Capacity", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Section (Action Instructions, Form Inputs, Action Buttons)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Instructions
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new GridLayout(3, 1, 2, 2));
        instructionPanel.setBorder(BorderFactory.createTitledBorder("📌 Action Instructions"));
        instructionPanel.add(new JLabel(" • ADD RECORD: Enter ALL information fields below."));
        instructionPanel.add(new JLabel(" • UPDATE RECORD: Enter Attraction ID + ONLY the specific fields."));
        instructionPanel.add(new JLabel(" • REMOVE RECORD: Enter ONLY the Attraction ID + click Remove."));
        instructionPanel.add(new JLabel(" • GUEST CALCULATION: Select to Calculate Daily Guest Capacity."));
        bottomPanel.add(instructionPanel);

        // Inputs
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Enter Attraction Details"));

        formPanel.add(new JLabel("Attraction ID:"));
        txtID = new JTextField();
        formPanel.add(txtID);

        formPanel.add(new JLabel("Attraction Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Park Location Zone:"));

        // Provides a dropdown menu for park location zone
        String[] locationOptions = new String[AttractionManager.VALID_ZONES.length + 1];
        locationOptions[0] = "-- Select Park Location Zone --";
        System.arraycopy(AttractionManager.VALID_ZONES, 0, locationOptions, 1, AttractionManager.VALID_ZONES.length);
        cmbLocation = new JComboBox<>(locationOptions);
        cmbLocation.setSelectedIndex(0);

        formPanel.add(cmbLocation);

        formPanel.add(new JLabel("Min Height (in):"));
        txtHeight = new JTextField();
        formPanel.add(txtHeight);

        formPanel.add(new JLabel("Daily Capacity:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);

        formPanel.add(new JLabel("Operational Status:"));
        chkActive = new JCheckBox("Is Active");
        formPanel.add(chkActive);

        bottomPanel.add(formPanel);

       // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Step 3: Select an Action"));

        JButton btnAdd = new JButton("➕ Add New Record");
        JButton btnUpdate = new JButton("✏️ Update Existing ID");
        JButton btnRemove = new JButton("❌ Remove by ID");
        JButton btnCustom = new JButton("📊 Daily Guest Calculation");
        JButton btnExit = new JButton("🚪 Exit Program");

        btnAdd.setBackground(new Color(187, 233, 208));
        btnAdd.setForeground(new Color(30, 70, 50));
        btnAdd.setOpaque(true);

        btnUpdate.setBackground(new Color(253, 237, 201));
        btnUpdate.setForeground(new Color(80, 60, 20));
        btnUpdate.setOpaque(true);

        btnRemove.setBackground(new Color(249, 202, 202));
        btnRemove.setForeground(new Color(90, 30, 30));
        btnRemove.setOpaque(true);

        btnCustom.setBackground(new Color(216, 196, 238));
        btnCustom.setForeground(new Color(50, 30, 70));
        btnCustom.setOpaque(true);

        btnExit.setBackground(new Color(225, 225, 225));
        btnExit.setForeground(new Color(50, 50, 50));
        btnExit.setOpaque(true);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnCustom);
        buttonPanel.add(btnExit);

        bottomPanel.add(buttonPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners & Validation

        btnLoadFile.addActionListener(e -> {
            int totalRecords = manager.getAttractionList().size();
            refreshTable();
            JOptionPane.showMessageDialog(this, totalRecords + " total attraction records currently loaded in the system!", "Status", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRefresh.addActionListener(e -> refreshTable());

        // Add Record Action with Validation
        btnAdd.addActionListener(e -> {
            try {
                // makes sure required fields aren't blank
                if (txtID.getText().trim().isEmpty() || txtName.getText().trim().isEmpty() ||
                        txtHeight.getText().trim().isEmpty() || txtCapacity.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("To Add a record, ALL fields must be filled out!");
                }
                // makes sure valid park zone is selected
                String location = (String) cmbLocation.getSelectedItem();
                if (location == null || location.startsWith("--")) {
                    throw new IllegalArgumentException("Please select a valid park location zone from the dropdown menu.");
                }

                int id = Integer.parseInt(txtID.getText().trim());
                String name = txtName.getText().trim();
                double height = Double.parseDouble(txtHeight.getText().trim());
                int capacity = Integer.parseInt(txtCapacity.getText().trim());
                boolean active = chkActive.isSelected();

                // Add Record
                AttractionRecord newRecord = new AttractionRecord(id, name, location, height, capacity, active);
                boolean added = manager.addRecord(newRecord);

                if (added) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Attraction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error! An attraction with ID " + id + " already exists.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error! ID, Height, and Capacity must be valid numeric values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error! " + ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Remove Record
        btnRemove.addActionListener(e -> {
            try {
                if (txtID.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "To remove a record, please enter the Attraction ID in the ID field above.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(txtID.getText().trim());
                boolean removed = manager.removeRecord(id);

                if (removed) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Attraction ID " + id + " removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Attraction ID " + id + " not found in system.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error! ID must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Record
        btnUpdate.addActionListener(e -> {
            try {
                if (txtID.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter the Attraction ID of the record you want to update.", "Input Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(txtID.getText().trim());
                AttractionRecord existing = manager.findRecordByID(id);

                if (existing == null) {
                    JOptionPane.showMessageDialog(this, "Attraction ID " + id + " not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Only update fields the user entered
                if (!txtName.getText().trim().isEmpty()) {
                    existing.setName(txtName.getText().trim());
                }

                String selectedLocation = (String) cmbLocation.getSelectedItem();
                if (selectedLocation != null && !selectedLocation.startsWith("--")) {
                    existing.setParkLocation(selectedLocation);
                }

                if (!txtHeight.getText().trim().isEmpty()) {
                    existing.setMinHeightRequirement(Double.parseDouble(txtHeight.getText().trim()));
                }

                if (!txtCapacity.getText().trim().isEmpty()) {
                    existing.setDailyCapacity(Integer.parseInt(txtCapacity.getText().trim()));
                }

                existing.setIsActive(chkActive.isSelected());

                refreshTable();
                JOptionPane.showMessageDialog(this, "Attraction ID " + id + " updated successfully (only entered fields were changed)!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error! Numeric fields must contain valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Custom Action: Daily Guest Capacity Throughout the Parks
        btnCustom.addActionListener(e -> {
            List<AttractionRecord> records = manager.getAttractionList();
            int calculatedSum = 0;
            for (AttractionRecord r : records) {
                calculatedSum += r.getDailyCapacity();
            }
            JOptionPane.showMessageDialog(this,
                    " Daily Guest Calculation - Total Combined Daily Capacity Across All Park Attractions:\n" + calculatedSum + " guests/day.",
                    "Daily Guest Calculation Results", JOptionPane.INFORMATION_MESSAGE);
        });

        // Exit Application
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // first point of application startup to load data and create the table view
        manager.loadDataFromFile("attractions.txt");
        refreshTable();
    }

    /**
     * To clear and reload all records from the collection into the JTable.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<AttractionRecord> records = manager.getAttractionList();
        for (AttractionRecord r : records) {
            Object[] row = {
                    r.getAttractionID(),
                    r.getName(),
                    r.getParkLocation(),
                    r.getMinHeightRequirement(),
                    r.getDailyCapacity(),
                    r.getIsActive()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * To reset all form input fields back to their blank states.
     */
    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        cmbLocation.setSelectedIndex(0);
        txtHeight.setText("");
        txtCapacity.setText("");
        chkActive.setSelected(false);
    }
}