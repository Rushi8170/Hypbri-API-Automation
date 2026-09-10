package com.framework.tests.api;

import com.framework.config.ConfigManager;
import com.framework.listeners.TestListener;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * BaseAPITest — parent class for all API tests.
 * Sets the REST Assured base URI before the suite runs.
 *
 * Note: ExtentReports init/flush is handled suite-wide by TestListener
 * (ISuiteListener.onStart/onFinish), so it is NOT duplicated here.
 */
@Listeners(TestListener.class)
public class BaseAPITest {
    protected static final Logger log = LogManager.getLogger(BaseAPITest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        RestAssured.baseURI = config.get("api.base.url");
        log.info("API Base URI: {}", RestAssured.baseURI);
    }
}
