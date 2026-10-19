package com.dcams.model;

/**
 * AttractionRecord represents an individual attraction or character meet-and-greet in the DCAMS.
 */
public class AttractionRecord {

    // Private attributes ensuring data encapsulation
    private int attractionID;
    private String name;
    private String parkLocation;
    private double minHeightRequirement;
    private int dailyCapacity;
    private boolean isActive;

    /**
     * Constructs a new AttractionRecord with all required attributes.
     */
    public AttractionRecord(int attractionID, String name, String parkLocation, double minHeightRequirement, int dailyCapacity, boolean isActive) {
        this.attractionID = attractionID;
        this.name = name;
        this.parkLocation = parkLocation;
        this.minHeightRequirement = minHeightRequirement;
        this.dailyCapacity = dailyCapacity;
        this.isActive = isActive;
    }

    /**
     * Retrieves the attraction ID.
     */
    public int getAttractionID() {
        return attractionID;
    }

    /**
     * Sets the attraction ID with validation.
     */
    public boolean setAttractionID(int attractionID) {
        if (attractionID > 0) {
            this.attractionID = attractionID;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the attraction name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the attraction name with null/empty checks.
     */
    public boolean setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the park zone location.
     */
    public String getParkLocation() {
        return parkLocation;
    }

    /**
     * Sets the park location.
     */
    public boolean setParkLocation(String parkLocation) {
        if (parkLocation != null && !parkLocation.trim().isEmpty()) {
            this.parkLocation = parkLocation;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the minimum height requirement.
     */

    public double getMinHeightRequirement() {
        return minHeightRequirement;
    }

    /**
     * Sets minimum height with range validation, it must be >= 0.
     */
    public boolean setMinHeightRequirement(double minHeightRequirement) {
        if (minHeightRequirement >= 0.0) {
            this.minHeightRequirement = minHeightRequirement;
            return true;
        }
        return false;
    }

    /**
     * Retrieves daily capacity.
     */
    public int getDailyCapacity() {
        return dailyCapacity;
    }

    /**
     * Sets daily capacity it must be >= 0.
     */
    public boolean setDailyCapacity(int dailyCapacity) {
        if (dailyCapacity >= 0) {
            this.dailyCapacity = dailyCapacity;
            return true;
        }
        return false;
    }

    /**
     * Checks if the attraction is currently active.
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Sets the active operational status flag.
     */
    public boolean setIsActive(boolean active) {
        this.isActive = active;
        return true;
    }

    /**
     * Formats the attraction record into clean readable string.
     */
    @Override
    public String toString() {
        return String.format("ID: %-4d | Name: %-28s | Location: %-32s | Height: %-4.1fin | Capacity: %-6d | Active: %-5b",
                attractionID, name, parkLocation, minHeightRequirement, dailyCapacity, isActive);
    }
}

