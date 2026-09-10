package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtil — reads test data from .xlsx files using Apache POI.
 *
 * Usage:
 *   List<Map<String,String>> rows = ExcelUtil.getSheetData("path/to/TestData.xlsx", "Login");
 */
public final class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    private ExcelUtil() {}

    /**
     * Returns all data rows from the given sheet as a list of maps.
     * The first row is treated as the header (column names).
     */
    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            int colCount  = headerRow.getLastCellNum();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int c = 0; c < colCount; c++) {
                    String key   = getCellValue(headerRow.getCell(c));
                    String value = getCellValue(row.getCell(c));
                    rowData.put(key, value);
                }
                dataList.add(rowData);
            }

            log.info("Loaded {} rows from sheet '{}' in '{}'", dataList.size(), sheetName, filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return dataList;
    }

    /**
     * Returns sheet data as Object[][] — compatible with TestNG @DataProvider.
     */
    public static Object[][] getSheetDataAsArray(String filePath, String sheetName) {
        List<Map<String, String>> list = getSheetData(filePath, sheetName);
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
