package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * ExtentReportManager — manages ExtentReports lifecycle.
 * Thread-safe via ThreadLocal<ExtentTest> for parallel runs.
 */
public final class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            // Ensure reports directory exists
            new File("reports").mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.REPORT_PATH);
            spark.config().setTheme(Theme.DARK);
            spark.config().setReportName(FrameworkConstants.REPORT_NAME);
            spark.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
            spark.config().setEncoding("utf-8");

            extentReports = new ExtentReports();
            extentReports.attachReporter(spark);
            extentReports.setSystemInfo("OS",          System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Environment",  System.getProperty("env", "qa"));

            log.info("Extent report initialised at: {}", FrameworkConstants.REPORT_PATH);
        }
        return extentReports;
    }

    public static void createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        extentTest.set(test);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void removeTest() {
        extentTest.remove();
    }

    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Extent report flushed.");
        }
    }
}
