package com.cerberussolutions.enjin.forum.exporter;

import com.cerberussolutions.enjin.forum.exporter.driver.ChromeWebDriverManager;
import com.cerberussolutions.enjin.forum.exporter.dto.EnjinSite;
import com.cerberussolutions.enjin.forum.exporter.dto.Forum;
import com.cerberussolutions.enjin.forum.exporter.extractor.ForumsExtractor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public class EnjinForumCrawler {

    private final WebDriver webDriver;
    private final ForumsExtractor forumsExtractor;

    public EnjinForumCrawler() {
        webDriver = ChromeWebDriverManager.createWebDriver();
        this.forumsExtractor = new ForumsExtractor(webDriver);
    }

    public EnjinSite getForumPosts() {
        showLogInRequestPopup();
        waitForElement(By.id("enjinbar-content-loggedin"), Duration.ofMinutes(10));

        waitForElement(By.cssSelector(".c.forum"), Duration.ofMinutes(10));

        List<Forum> forums = forumsExtractor.extractForums();

        webDriver.quit();

        var enjinSite = new EnjinSite();
        enjinSite.setForums(forums);
        return enjinSite;
    }

    private void showLogInRequestPopup() {
        ((JavascriptExecutor) webDriver).executeScript(
                "alert('Log in to your enjin website, then open its forum.')");
        new FluentWait<>(webDriver)
                .withTimeout(Duration.ofMinutes(10))
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
    }

    private void waitForElement(By elementSelector, Duration waitDuration) {
        new FluentWait<>(webDriver)
                .withTimeout(waitDuration)
                .ignoring(NoSuchElementException.class)
                .pollingEvery(Duration.ofSeconds(1))
                .until(ExpectedConditions.presenceOfElementLocated(elementSelector));
    }

}
