package com.dcams.main;

import com.dcams.logic.AttractionManager;
import com.dcams.model.AttractionRecord;
import java.util.Scanner;

/**
 * Provides menu oop, input validation, exception handling, and user interaction
 */
public class MainApplication {
    private AttractionManager manager;
    private Scanner scanner;

    /**
     * initializes application controller and input scanner
     */
    public MainApplication() {
        this.manager = new AttractionManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * run interactive menu loop
     */
    public boolean runMenuLoop() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getValidIntegerInput("Enter your menu selection (1-7): ");

            switch (choice) {
                case 1:
                    handleBatchLoad();
                    break;
                case 2:
                    handleDisplayData();
                    break;
                case 3:
                    handleCreateRecord();
                    break;
                case 4:
                    handleUpdateRecord();
                    break;
                case 5:
                    handleRemoveRecord();
                    break;
                case 6:
                    handleCustomAction();
                    break;
                case 7:
                    System.out.println("Exiting DCAMS. Have a magical day!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option. Please select between numbers 1 and 7.");
            }
        }
        return true;
    }

    /**
     * displays operations menu to the console
     */
    private void printMenu() {
        System.out.println("\n======================================================================");
        System.out.println("  DCAMS (Disney Character & Attraction Management System)");
        System.out.println("  To manage resort attractions, guest capacities,");
        System.out.println("  safety parameters, and operational throughput projections.");
        System.out.println("======================================================================");
        System.out.println("1. Read Data");
        System.out.println("2. Display All Data");
        System.out.println("3. Create Data");
        System.out.println("4. Update Data");
        System.out.println("5. Remove Data");
        System.out.println("6. Calculate Zone Projected Throughput");
        System.out.println("7. Exit Program");
        System.out.println("----------------------------------------------------------------------");
    }

    /**
     * handles batch file loading interaction
     */
    private void handleBatchLoad() {
        System.out.print("Enter text file path: ");
        String filePath = scanner.nextLine().trim();
        int count = manager.loadDataFromFile(filePath);
        System.out.println("Successfully loaded " + count + " attraction records into the system.");
    }

    /**
     * displays all active records in memory
     */
    private void handleDisplayData() {
        if (manager.getAttractionList().isEmpty()) {
            System.out.println("No attraction records currently exist in the system.");
            return;
        }
        System.out.println("\n Current DCAMS Attraction Records ");
        for (AttractionRecord record : manager.getAttractionList()) {
            System.out.println(record);
        }
    }

    /**
     * manual creation of new attraction record
     */
    private void handleCreateRecord() {
        System.out.println("\n Add New Attraction Record ");
        int id = getValidIntegerInput("Enter unique Attraction ID (integer): ");
        if (manager.findRecordByID(id) != null) {
            System.out.println("Error! An attraction with ID " + id + " already exists.");
            return;
        }

        System.out.print("Enter Attraction/Character Name: ");
        String name = scanner.nextLine().trim();

        String location = selectParkZoneMenu();
        double height = getValidHeightInput();
        int capacity = getValidIntegerInput("Enter Daily Guest Capacity (integer): ");
        boolean active = getValidBooleanInput("Is Attraction Active? (true/false): ");

        AttractionRecord newRecord = new AttractionRecord(id, name, location, height, capacity, active);
        if (manager.addRecord(newRecord)) {
            System.out.println("Success! Attraction record added.");
        } else {
            System.out.println("Failed to add record due to invalid data entered.");
        }
    }

    /**
     * updates exisiting attraction record
     */
    private void handleUpdateRecord() {
        System.out.println("\n Update Attraction Record ");
        int id = getValidIntegerInput("Enter Attraction ID to update: ");
        AttractionRecord record = manager.findRecordByID(id);
        if (record == null) {
            System.out.println("Error! Attraction ID " + id + " not found.");
            return;
        }

        System.out.println("Current Record: " + record);
        System.out.println("\nSelect field to update:");
        System.out.println("1. Name");
        System.out.println("2. Park Location");
        System.out.println("3. Minimum Height Requirement");
        System.out.println("4. Daily Capacity");
        System.out.println("5. Active Status");

        int choice = getValidIntegerInput("Enter choice (1-5): ");
        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine().trim();
                record.setName(newName);
                System.out.println("Success! Name updated.");
                break;
            case 2:
                String newLocation = selectParkZoneMenu();
                record.setParkLocation(newLocation);
                System.out.println("Success! Location updated.");
                break;
            case 3:
                double newHeight = getValidHeightInput();
                record.setMinHeightRequirement(newHeight);
                System.out.println("Success! Height requirement updated.");
                break;
            case 4:
                int newCapacity = getValidIntegerInput("Enter new Daily Capacity: ");
                record.setDailyCapacity(newCapacity);
                System.out.println("Success! Daily capacity updated.");
                break;
            case 5:
                boolean newActive = getValidBooleanInput("Is Attraction Active? (true/false): ");
                record.setIsActive(newActive);
                System.out.println("Success! Active status updated.");
                break;
            default:
                System.out.println("Invalid field selection.");
        }
    }

    /**
     * remove attraction record by ID
     */
    private void handleRemoveRecord() {
        System.out.println("\n Remove Attraction Record ");
        int id = getValidIntegerInput("Enter Attraction ID to remove: ");
        if (manager.removeRecord(id)) {
            System.out.println("Success! Attraction ID " + id + " was removed from the system.");
        } else {
            System.out.println("Error! Attraction ID " + id + " not found.");
        }
    }

    /**
     * execute and display the custom throughput calculation action
     */
    private void handleCustomAction() {
        System.out.println("\n Zone Projected Throughput Calculation ");
        String zone = selectParkZoneMenu();

        double throughput = manager.calculateProjectedThroughput(zone);
        System.out.printf("Projected Daily Guest Throughput for [%s] (at 85%% operational efficiency): %.2f guests\n", zone, throughput);
    }

    /**
     * Secondary menu preventing user typos by offering a numbered list of park zones.
     */
    private String selectParkZoneMenu() {
        System.out.println("\n Select Park Zone ");
        for (int i = 0; i < AttractionManager.VALID_ZONES.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), AttractionManager.VALID_ZONES[i]);
        }
        while (true) {
            int zoneChoice = getValidIntegerInput("Select zone number: ");
            if (zoneChoice >= 1 && zoneChoice <= AttractionManager.VALID_ZONES.length) {
                return AttractionManager.VALID_ZONES[zoneChoice - 1];
            }
            System.out.println("Invalid selection. Please choose a number between 1 and " + AttractionManager.VALID_ZONES.length + ".");
        }
    }

    private double getValidHeightInput() {
        while (true) {
            double height = getValidDoubleInput("Enter Minimum Height Requirement in inches (0.0 - 72.0): ");
            if (height >= 0.0 && height <= 72.0) {
                return height;
            }
            System.out.println("Error! Height must be between 0.0 and 72.0 inches.");
        }
    }

    /**
     * validation loop to prevent crashes for invalid integer inputs
     */
    private int getValidIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * validation loop to prevent invalid double inputs
     */
    private double getValidDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal.");
            }
        }
    }

    /**
     * validation loop to ensure boolean input
     */
    private boolean getValidBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            }
            System.out.println("Invalid input. Please enter exactly 'true' or 'false'.");
        }
    }

    /**
     * main method to launch the application
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.runMenuLoop();
    }
}