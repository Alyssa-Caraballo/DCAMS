package com.dcams.logic;

import com.dcams.model.AttractionRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AttractionManager handles in-memory list collections, CRUD operations,
 * batch file text reading, and custom zone throughput calculations
 */
public class AttractionManager {
    // Java Collection managing in-memory attraction objects
    private List<AttractionRecord> attractionList;

    /**
     * second menu for park zones, users won't have to type in the zones themselves which eliminates typos
     */
    public static final String[] VALID_ZONES = {
            "Magic Kingdom - Tomorrowland",
            "Magic Kingdom - Frontierland",
            "Magic Kingdom - Fantasyland",
            "Magic Kingdom - Adventureland",
            "Magic Kingdom - Liberty Square",
            "Disney's Hollywood Studios - Galaxy's Edge",
            "Disney's Hollywood Studios - Toy Story Land",
            "Disney's Hollywood Studios - Sunset Boulevard",
            "Disney's Animal Kingdom - Pandora",
            "Disney's Animal Kingdom - Asia",
            "Disney's Animal Kingdom - Africa",
            "Disney's Animal Kingdom - DinoLand U.S.A."
    };

    /**
     * Initializes AttractionManager and its ArrayList collection
     */
    public AttractionManager() {
        this.attractionList = new ArrayList<>();
    }

    /**
     * returns active collection list
     */
    public List<AttractionRecord> getAttractionList() {
        return attractionList;
    }

    /**
     * Adds an attraction record in memory if it doesn't already exist
     */
    public boolean addRecord(AttractionRecord record) {
        if (record != null && findRecordByID(record.getAttractionID()) == null) {
            attractionList.add(record);
            return true;
        }
        return false;
    }

    /**
     * searches for record by unique ID
     */
    public AttractionRecord findRecordByID(int id) {
        for (AttractionRecord record : attractionList) {
            if (record.getAttractionID() == id) {
                return record;
            }
        }
        return null;
    }

    /**
     * Removes a record by ID
     */
    public boolean removeRecord(int id) {
        AttractionRecord record = findRecordByID(id);
        if (record != null) {
            attractionList.remove(record);
            return true;
        }
        return false;
    }

    /**
     * updates an attractions daily capacity
     */
    public boolean updateRecordCapacity(int id, int newCapacity) {
        AttractionRecord record = findRecordByID(id);
        if (record != null && newCapacity >= 0) {
            record.setDailyCapacity(newCapacity);
            return true;
        }
        return false;
    }

    /**
     * loads records from a text file path.
     */
    public int loadDataFromFile(String filePath) {
        int loadedCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String location = parts[2].trim();
                        double height = Double.parseDouble(parts[3].trim());
                        int capacity = Integer.parseInt(parts[4].trim());
                        boolean active = Boolean.parseBoolean(parts[5].trim());

                        AttractionRecord newRecord = new AttractionRecord(id, name, location, height, capacity, active);
                        if (addRecord(newRecord)) {
                            loadedCount++;
                        }
                    } catch (NumberFormatException e) {
                        // Skip malformed rows
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return loadedCount;
    }

    /**
     * Custom Action: Calculates projected guest throughput for a specific park zone
     * by summing active daily capacities and multiplying by an 85% operating efficiency factor
     */
    public double calculateProjectedThroughput(String parkLocation) {
        double totalCapacity = 0.0;
        for (AttractionRecord record : attractionList) {
            if (record.getIsActive() && record.getParkLocation().equalsIgnoreCase(parkLocation.trim())) {
                totalCapacity += record.getDailyCapacity();
            }
        }
        return totalCapacity * 0.85;
    }
}