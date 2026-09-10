package com.framework.listeners;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

/**
 * TestListener — plugs Extent Reports into TestNG.
 * Register in testng XML: <listener class-name="com.framework.listeners.TestListener"/>
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    // ── Suite ────────────────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        log.info("===== Suite started: {} =====", suite.getName());
        ExtentReportManager.getInstance(); // initialise
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("===== Suite finished: {} =====", suite.getName());
        ExtentReportManager.flushReports();
    }

    // ── Test ─────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentReportManager.createTest(testName, description);
        log.info("TEST STARTED: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.getTest().log(Status.PASS, "Test PASSED: " + testName);
        log.info("TEST PASSED: {}", testName);
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("TEST FAILED: {} — {}", testName, result.getThrowable().getMessage());
        ExtentReportManager.getTest().fail(result.getThrowable());
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.getTest().log(Status.SKIP,
                "Test SKIPPED: " + testName + " — " + result.getThrowable());
        log.warn("TEST SKIPPED: {}", testName);
        ExtentReportManager.removeTest();
    }
}

