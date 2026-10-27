package com.dcams.logic;

import com.dcams.model.AttractionRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AttractionManagerTest contains JUnit tests verifying file opening,
 * CRUD operations, and custom throughput calculations.
 */
public class AttractionManagerTest {
    private AttractionManager manager;

    @BeforeEach
    public void setUp() {
        manager = new AttractionManager();
    }

    @Test
    public void testLoadDataFromFileSuccess() {
        // Test opening and loading from attractions.txt
        int count = manager.loadDataFromFile("attractions.txt");
        assertTrue(count > 0, "File should open and successfully load records.");
    }

    @Test
    public void testLoadDataFromFileInvalidPath() {
        // Negative test case for bad file name
        int count = manager.loadDataFromFile("nonexistent_file.txt");
        assertEquals(0, count, "Loading non-existent file should return 0 records.");
    }

    @Test
    public void testAddRecordPositive() {
        AttractionRecord record = new AttractionRecord(999, "Test Coaster", "Magic Kingdom - Tomorrowland", 40.0, 10000, true);
        boolean added = manager.addRecord(record);
        assertTrue(added, "Adding a new unique record should return true.");
        assertNotNull(manager.findRecordByID(999), "Record should exist in manager list.");
    }

    @Test
    public void testAddRecordNegativeDuplicate() {
        AttractionRecord record1 = new AttractionRecord(999, "Test Coaster", "Magic Kingdom - Tomorrowland", 40.0, 10000, true);
        AttractionRecord record2 = new AttractionRecord(999, "Duplicate Coaster", "Magic Kingdom - Fantasyland", 0.0, 5000, true);

        manager.addRecord(record1);
        boolean addedDuplicate = manager.addRecord(record2);

        assertFalse(addedDuplicate, "Adding a record with an existing ID should return false.");
    }

    @Test
    public void testRemoveRecordPositive() {
        AttractionRecord record = new AttractionRecord(888, "Temp Ride", "Magic Kingdom - Adventureland", 0.0, 8000, true);
        manager.addRecord(record);

        boolean removed = manager.removeRecord(888);
        assertTrue(removed, "Removing an existing record should return true.");
        assertNull(manager.findRecordByID(888), "Record should no longer exist.");
    }

    @Test
    public void testRemoveRecordNegative() {
        boolean removed = manager.removeRecord(5555);
        assertFalse(removed, "Removing non-existent record ID should return false.");
    }

    @Test
    public void testUpdateRecordAttributes() {
        AttractionRecord record = new AttractionRecord(777, "Space Drop", "Disney's Hollywood Studios - Sunset Boulevard", 48.0, 15000, true);
        manager.addRecord(record);

        // Update fields
        record.setDailyCapacity(20000);
        record.setName("Updated Space Drop");

        assertEquals(20000, manager.findRecordByID(777).getDailyCapacity(), "Daily capacity should be updated.");
        assertEquals("Updated Space Drop", manager.findRecordByID(777).getName(), "Name should be updated.");
    }

    @Test
    public void testCustomActionThroughputCalculation() {
        AttractionRecord r1 = new AttractionRecord(1, "Ride A", "Tomorrowland", 0.0, 10000, true);
        AttractionRecord r2 = new AttractionRecord(2, "Ride B", "Tomorrowland", 0.0, 10000, true);

        manager.addRecord(r1);
        manager.addRecord(r2);

        double throughput = manager.calculateProjectedThroughput("Tomorrowland");
        // Total capacity = 20000 * 0.85 = 17000.0
        assertEquals(17000.0, throughput, 0.001, "Projected throughput calculation should equal capacity * 0.85.");
    }
}