package com.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JsonUtil — reads JSON test data files using Jackson.
 *
 * Usage:
 *   Map<String,Object> data = JsonUtil.readJsonFile("path/to/file.json");
 *   MyPojo pojo = JsonUtil.readJsonFile("path/to/file.json", MyPojo.class);
 */
public final class JsonUtil {

    private static final Logger log = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {}

    @SuppressWarnings("unchecked")
    public static Map<String, Object> readJsonFile(String filePath) {
        try {
            return MAPPER.readValue(new File(filePath), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try {
            return MAPPER.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> readJsonArray(String filePath) {
        try {
            return MAPPER.readValue(new File(filePath), List.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON array from: " + filePath, e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public static <T> T fromJsonString(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON string", e);
        }
    }
}
