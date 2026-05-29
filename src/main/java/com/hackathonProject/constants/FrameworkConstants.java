package com.hackathonproject.constants;

import com.hackathonproject.utils.ConfigReader;

public class FrameworkConstants {

    // From config.properties
    public static final String BASE_URL = ConfigReader.getProperty("baseUrl");
    public static final String EXCEL_OUTPUT_PATH = ConfigReader.getProperty("excelOutputPath");
    public static final String EXTENT_REPORT_PATH = "reports/extent/ExtentReport.html";

    private FrameworkConstants() {}
}