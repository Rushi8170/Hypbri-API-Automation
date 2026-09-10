package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/api",
        glue     = "com.framework.stepdefs",
        tags     = "@api",
        plugin   = {
            "pretty",
            "html:reports/cucumber-api-report.html",
            "json:reports/cucumber-api.json"
        },
        monochrome = true
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
