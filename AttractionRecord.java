package com.dcams.model;

/**
 * AttractionRecord represents an individual attraction or character meet-and-greet in DCAMS.
 */
public class AttractionRecord {
    private int attractionID;
    private String name;
    private String parkLocation;
    private double minHeightRequirement;
    private int dailyCapacity;
    private boolean isActive;

    /**
     Constructs new AttractionRecord
     */

    public AttractionRecord(int attractionID, String name, String parkLocation, double minHeightRequirement, int dailyCapacity, boolean isActive) {
        this.attractionID = attractionID;
        this.name = name;
        this.parkLocation = parkLocation;
        this.minHeightRequirement = minHeightRequirement;
        this.dailyCapacity = dailyCapacity;
        this.isActive = isActive;
    }

    public int getAttractionID() {
        return attractionID;
    }

    /**
     * set attraction ID with validation
     */
    public boolean setAttractionID(int attractionID) {
        if (attractionID > 0) {
            this.attractionID = attractionID;
            return true;
        }
        return false;
    }

    /**
     * retrieves attraction name
     */
    public String getName() {
        return name;
    }

    /**
     *set attraction name with null/empty checks
     */
    public boolean setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
            return true;
        }
        return false;
    }

    /**
     * retrieves park zone Location
     */
    public String getParkLocation() {
        return parkLocation;
    }

    /**
     * sets park location
     */
    public boolean setParkLocation(String parkLocation) {
        if (parkLocation != null && !parkLocation.trim().isEmpty()) {
            this.parkLocation = parkLocation;
            return true;
        }
        return false;
    }

    /**
     * retrieve min height requirement
     */
    public double getMinHeightRequirement() {
        return minHeightRequirement;
    }

    /**
     * set min height requirement with range validation, must be >= 0
     */
    public boolean setMinHeightRequirement(double minHeightRequirement) {
        if (minHeightRequirement >= 0.0 && minHeightRequirement <= 72.0) {
            this.minHeightRequirement = minHeightRequirement;
            return true;
        }
        return false;
    }

    /**
     * retrieve daily capacity
     */
    public int getDailyCapacity() {
        return dailyCapacity;
    }

    /**
     * set daily capacity must be >= 0
     */
    public boolean setDailyCapacity(int dailyCapacity) {
        if (dailyCapacity >= 0) {
            this.dailyCapacity = dailyCapacity;
            return true;
        }
        return false;
    }

    /**
     * check if attraction is active
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * set active operational status flag
     */
    public boolean setIsActive(boolean active) {
        this.isActive = active;
        return true;
    }

    /**
     * format attraction record into clean string
     */
    @Override
    public String toString() {
        return String.format("ID: %-4d | Name: %-28s | Location: %-38s | Height: %-4.1fin | Capacity: %-6d | Active: %-5b",
                attractionID, name, parkLocation, minHeightRequirement, dailyCapacity, isActive);
    }
}