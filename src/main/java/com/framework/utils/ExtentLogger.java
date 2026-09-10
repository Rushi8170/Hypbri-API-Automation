package com.framework.utils;

import com.aventstack.extentreports.Status;
import com.framework.listeners.ExtentReportManager;

/**
 * ExtentLogger — convenience class to log messages into Extent Report
 * from test steps, step defs, or anywhere in the framework.
 *
 * Usage:
 *   ExtentLogger.pass("Login API returned 200");
 *   ExtentLogger.fail("Unexpected status code");
 *   ExtentLogger.info("Sending POST request");
 */
public final class ExtentLogger {

    private ExtentLogger() {}

    public static void pass(String message) {
        ExtentReportManager.getTest().log(Status.PASS, message);
    }

    public static void fail(String message) {
        ExtentReportManager.getTest().log(Status.FAIL, message);
    }

    public static void info(String message) {
        ExtentReportManager.getTest().log(Status.INFO, message);
    }

    public static void warning(String message) {
        ExtentReportManager.getTest().log(Status.WARNING, message);
    }

    public static void skip(String message) {
        ExtentReportManager.getTest().log(Status.SKIP, message);
    }
}
