package com.cerberussolutions.enjin.forum.exporter.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ChromeWebDriverManager {

    public static WebDriver createWebDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("safebrowsing.enabled", true);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments(
                "--disable-extentions",
                "--safebrowsing-disable-download-protection",
                "--safebrowsing-disable-extension-blacklist"
        );

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.BROWSER, Level.WARNING);
        logs.enable(LogType.BROWSER, Level.SEVERE);
        options.setCapability(CapabilityType.LOGGING_PREFS, logs);
        options.setCapability(CapabilityType.OVERLAPPING_CHECK_DISABLED, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);

        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        ChromeDriver chromeDriver = new ChromeDriver(chromeDriverService, options);

        return chromeDriver;
    }

}
