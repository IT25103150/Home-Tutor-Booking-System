package com.tutorbooking.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared utility for file handling to ensure clean team code.
 * Implements CRUD operations for TXT files using "|" as delimiter.
 */
public class FileHelper {
    private static final String DATA_DIR = "src/main/resources/data/";
    private static final String DELIMITER_REGEX = "\\|";

    /**
     * Reads all lines from a file.
     */
    public static List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(DATA_DIR + fileName);
            if (!file.exists()) {
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    /**
     * Appends a single line (record) to a file.
     */
    public static void appendLine(String fileName, String line) {
        try {
            File file = new File(DATA_DIR + fileName);
            if (!file.exists()) {
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error appending to file " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Updates a record by ID (assuming ID is the first column).
     */
    public static boolean updateRecordById(String fileName, String id, String newRecord) {
        List<String> lines = readAllLines(fileName);
        boolean updated = false;
        List<String> newLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(DELIMITER_REGEX);
            if (parts.length > 0 && parts[0].equals(id)) {
                newLines.add(newRecord);
                updated = true;
            } else {
                newLines.add(line);
            }
        }

        if (updated) {
            writeAllLines(fileName, newLines);
        }
        return updated;
    }

    /**
     * Deletes a record by ID.
     */
    public static boolean deleteRecordById(String fileName, String id) {
        List<String> lines = readAllLines(fileName);
        List<String> newLines = new ArrayList<>();
        boolean deleted = false;

        for (String line : lines) {
            String[] parts = line.split(DELIMITER_REGEX);
            if (parts.length > 0 && parts[0].equals(id)) {
                deleted = true;
            } else {
                newLines.add(line);
            }
        }

        if (deleted) {
            writeAllLines(fileName, newLines);
        }
        return deleted;
    }

    /**
     * Overwrites all lines in a file.
     */
    public static void writeAllLines(String fileName, List<String> lines) {
        try {
            File file = new File(DATA_DIR + fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + fileName + ": " + e.getMessage());
        }
    }
}
