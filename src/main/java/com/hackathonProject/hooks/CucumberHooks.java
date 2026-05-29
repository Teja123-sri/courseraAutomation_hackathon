package com.hackathonproject.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hackathonproject.base.BaseClass;
import com.hackathonproject.utils.ExtentReportManager;
import com.hackathonproject.utils.ScreenshotUtil;

public class CucumberHooks {

    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void setUp(Scenario scenario) {
        logger.info("====== SCENARIO STARTED: " + scenario.getName() + " ======");
        logger.info("Tags: " + scenario.getSourceTagNames());

        // Launch browser (reads 'browser' from config.properties)
        BaseClass.createDriver();

        // Initialize Extent Report test node for this scenario
        ExtentReportManager.createTest(scenario.getName());
        ExtentReportManager.logInfo("Browser launched for scenario: " + scenario.getName());

        logger.info("Browser launched successfully");
    }

    @After
    public void tearDown(Scenario scenario) {

        logger.info("====== SCENARIO FINISHED: " + scenario.getName()
                    + " | Status: " + scenario.getStatus() + " ======");

        if (BaseClass.getDriver() != null) {

            String safeName = scenario.getName()
                                  .replaceAll("[^a-zA-Z0-9]", "_")
                                  .replaceAll("_+", "_");

            if (scenario.isFailed()) {
                logger.warn("Scenario FAILED — capturing screenshot");

                byte[] screenshotBytes = ScreenshotUtil
                    .captureScreenshotAsBytes(BaseClass.getDriver());
                if (screenshotBytes != null) {
                    scenario.attach(screenshotBytes, "image/png", "Failure Screenshot");
                }

                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    BaseClass.getDriver(), safeName + "_FAILED"
                );
                ExtentReportManager.logFail("Scenario FAILED: " + scenario.getName());
                ExtentReportManager.attachScreenshot(screenshotPath);

            } else {
                logger.info("Scenario PASSED — capturing screenshot");

                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    BaseClass.getDriver(), safeName + "_PASSED"
                );
                ExtentReportManager.logPass("Scenario PASSED: " + scenario.getName());
                ExtentReportManager.attachScreenshot(screenshotPath);
            }

        } else {
            logger.warn("Driver is null — skipping screenshot for: "
                        + scenario.getName());
            if (scenario.isFailed()) {
                ExtentReportManager.logFail(
                    "Scenario FAILED (driver never launched): " + scenario.getName());
            }
        }

        BaseClass.removeDriver();
        logger.info("Browser closed. Scenario teardown complete.");
    }
}
