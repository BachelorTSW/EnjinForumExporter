package com.cerberussolutions.enjin.forum.exporter;

import com.cerberussolutions.enjin.forum.exporter.driver.ChromeWebDriverManager;
import com.cerberussolutions.enjin.forum.exporter.dto.EnjinSite;
import com.cerberussolutions.enjin.forum.exporter.dto.Forum;
import com.cerberussolutions.enjin.forum.exporter.dto.Post;
import com.cerberussolutions.enjin.forum.exporter.dto.Thread;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EnjinForumCrawler {

    WebDriver webDriver;

    public EnjinForumCrawler() {
        webDriver = ChromeWebDriverManager.createWebDriver();
    }

    public EnjinSite getForumPosts() {
        showLogInRequestPopup();
        waitForElement(By.id("enjinbar-content-loggedin"), Duration.ofMinutes(10));

        List<Forum> forums = extractForums();

        webDriver.quit();

        var enjinSite = new EnjinSite();
        enjinSite.setForums(forums);
        return enjinSite;
    }

    private List<Forum> extractForums() {
        goToRelativeUrl("/forum");
        List<Forum> forums = new ArrayList<>();
        for (var forumElement : webDriver.findElements(By.cssSelector(".c.forum"))) {
            var forum = new Forum();
            var forumNameElement = forumElement.findElement(By.cssSelector(".forum-name"));
            forum.setName(forumNameElement.getText());
            forum.setUrl(forumNameElement.getAttribute("href"));
            forum.setDescription(forumElement.findElement(By.cssSelector(".description")).getText());
            forums.add(forum);
        }

        for (var forum : forums) {
            forum.setThreads(extractThreads(forum.getUrl()));
        }

        return forums;
    }

    private List<Thread> extractThreads(String forumUrl) {
        goToRelativeUrl(forumUrl);
        List<Thread> threads = new ArrayList<>();
        for (var threadElement : webDriver.findElements(By.cssSelector(".c.thread"))) {
            var thread = new Thread();
            var threadNameElement = threadElement.findElement(By.cssSelector(".thread-view"));
            thread.setName(threadNameElement.getText());
            thread.setUrl(threadNameElement.getAttribute("href"));
            thread.setAuthor(threadElement.findElement(By.cssSelector(".element_username")).getText());
            threads.add(thread);
        }

        for (var thread : threads) {
            thread.setPosts(extractPosts(thread.getUrl()));
        }

        return threads;
    }

    private List<Post> extractPosts(String threadUrl) {
        goToRelativeUrl(threadUrl);
        List<Post> posts = new ArrayList<>();
        for (var postElement : webDriver.findElements(By.xpath("//*[@post_id]"))) {
            var post = new Post();
            post.setAuthor(postElement.findElement(By.cssSelector(".element_username")).getText());
            post.setContent(postElement.findElement(By.cssSelector(".post-content")).getAttribute("innerHTML"));
            posts.add(post);
        }
        return posts;
    }


    private void goToRelativeUrl(String url) {
        try {
            webDriver.get(new URI(webDriver.getCurrentUrl()).resolve(url).toString());
        } catch (URISyntaxException ignored) {
        }
    }

    private void showLogInRequestPopup() {
        ((JavascriptExecutor) webDriver).executeScript(
                "alert('Open your enjin website and log in with your credentials.')");
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
