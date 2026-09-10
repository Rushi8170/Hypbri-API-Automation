package com.framework.constants;

/**
 * Framework-wide constants.
 * Add new constants here — never hardcode values in tests.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {}

    // ── Paths ───────────────────────────────────────────────────
    public static final String CONFIG_DIR        = "src/main/resources/config/";
    public static final String REPORT_PATH       = "reports/ExtentReport.html";
    public static final String TESTDATA_EXCEL    = "src/test/resources/testdata/TestData.xlsx";
    public static final String LOG_DIR           = "logs/";

    // ── Report ──────────────────────────────────────────────────
    public static final String REPORT_NAME  = "API Automation Report";
    public static final String REPORT_TITLE = "Test Execution Report";

    // ── Sheet Names (Excel) ─────────────────────────────────────
    public static final String SHEET_LOGIN  = "Login";
    public static final String SHEET_USER   = "Users";
}
