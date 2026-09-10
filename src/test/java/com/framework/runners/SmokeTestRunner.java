package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/api",
        glue     = "com.framework.stepdefs",
        tags     = "@smoke",
        plugin   = {
            "pretty",
            "html:reports/cucumber-smoke-report.html",
            "json:reports/cucumber-smoke.json"
        },
        monochrome = true
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
}
