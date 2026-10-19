package com.dcams.main;

import com.dcams.logic.AttractionManager;
import com.dcams.model.AttractionRecord;
import java.util.Scanner;

/**
 * Provides menu loop,input validation, exception handling, and user interaction.
 */
public class MainApplication {
    private AttractionManager manager;
    private Scanner scanner;

    /**
     * Initializes the application controller and input scanner.
     */
    public MainApplication() {
        this.manager = new AttractionManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the interactive menu loop for operation.
     */
    public boolean runMenuLoop() {
        boolean running = true;
        System.out.println(" Welcome to DCAMS! (Disney Character & Attraction Management System) ");

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
                    System.out.println("Invalid menu option. Please select numbers from 1 through 7.");
            }
        }
        return true;
    }

    /**
     * Displays the operations menu to the console.
     */
    private void printMenu() {
        System.out.println("\n");
        System.out.println("1. Read Data");
        System.out.println("2. Display All Data");
        System.out.println("3. Create Data");
        System.out.println("4. Update Data");
        System.out.println("5. Remove Data");
        System.out.println("6. Calculate Zone Projected Throughput");
        System.out.println("7. Exit Program");
    }

    /**
     * Handles batch file loading interaction.
     */
    private void handleBatchLoad() {
        System.out.print("Enter text file path: ");
        String filePath = scanner.nextLine().trim();
        int count = manager.loadDataFromFile(filePath);
        System.out.println("Successfully loaded " + count + " attraction records into the system.");
    }

    /**
     * Displays all active records currently in memory.
     */
    private void handleDisplayData() {
        if (manager.getAttractionList().isEmpty()) {
            System.out.println("No attraction records currently exist in the system.");
            return;
        }
        System.out.println("\n Current DCAMS Attraction Records");
        for (AttractionRecord record : manager.getAttractionList()) {
            System.out.println(record);
        }
    }

    /**
     * Manual creation of a new attraction record
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

        System.out.print("Enter Park Location (example: Magic Kingdom - Tomorrowland): ");
        String location = scanner.nextLine().trim();

        double height = getValidDoubleInput("Enter Minimum Height Requirement in inches (example: 40.0): ");
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
     * Updates an existing attraction record's capacity.
     */
    private void handleUpdateRecord() {
        System.out.println("\n Update Attraction Capacity ");
        int id = getValidIntegerInput("Enter Attraction ID to update: ");
        AttractionRecord record = manager.findRecordByID(id);
        if (record == null) {
            System.out.println("Error! Attraction ID " + id + " not found.");
            return;
        }

        System.out.println("Current Record: " + record);
        int newCapacity = getValidIntegerInput("Enter New Daily Capacity Value: ");
        if (manager.updateRecordCapacity(id, newCapacity)) {
            System.out.println("Success! Capacity updated.");
        } else {
            System.out.println("Error! Invalid capacity value provided.");
        }
    }

    /**
     * Removes an attraction record by ID.
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
     * Executes and displays the custom throughput calculation action.
     */
    private void handleCustomAction() {
        System.out.println("\n Zone Projected Throughput Calculation ");
        System.out.print("Enter Park Zone (example: Magic Kingdom - Tomorrowland): ");
        String zone = scanner.nextLine().trim();

        double throughput = manager.calculateProjectedThroughput(zone);
        System.out.printf("Projected Daily Guest Throughput for [%s] (at 85%% operational efficiency): %.2f guests\n", zone, throughput);
    }

    /**
     * Validation loop to prevent crashes from invalid integer inputs.
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
     * Validation loop to prevent crashes from invalid double inputs.
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
     * Validation loop to ensure boolean input.
     */
    private boolean getValidBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            }
            System.out.println("Invalid input. Please enter only 'true' or 'false'.");
        }
    }

    /**
     * main method to launch the application.
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.runMenuLoop();
    }
}