package com.cerberussolutions.enjin.forum.exporter.extractor;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractExtractor {

    private final Pattern paginationUrlPattern = Pattern.compile("document.location=['\"](?<url>.*)['\"];?");
    protected final WebDriver webDriver;

    protected AbstractExtractor(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    protected String getNextPageUrlFromPagination(WebElement paginationElement) {
        Matcher urlMatcher = paginationUrlPattern.matcher(paginationElement.getAttribute("onclick"));
        if (urlMatcher.find()) {
            return urlMatcher.group("url");
        }
        return null;
    }

    protected void goToRelativeUrl(String url) {
        try {
            webDriver.get(new URI(webDriver.getCurrentUrl()).resolve(url).toString());
        } catch (URISyntaxException ignored) {
        }

        // Avoid getting throttled by Enjin
        try {
            java.lang.Thread.sleep(1000);
        } catch (InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
        }
    }

    protected void waitForElement(By elementSelector, Duration waitDuration) {
        new FluentWait<>(webDriver)
                .withTimeout(waitDuration)
                .ignoring(NoSuchElementException.class)
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.presenceOfElementLocated(elementSelector));
    }

}
