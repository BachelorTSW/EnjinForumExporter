package com.cerberussolutions.enjin.forum.exporter.extractor;

import com.cerberussolutions.enjin.forum.exporter.dto.Thread;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ThreadsExtractor extends AbstractExtractor {

    private final PostsExtractor postsExtractor;

    public ThreadsExtractor(WebDriver webDriver) {
        super(webDriver);
        this.postsExtractor = new PostsExtractor(webDriver);
    }

    public List<Thread> extractForumThreads(String forumUrl) {
        Optional<String> currentPaginationPageUrl = Optional.ofNullable(forumUrl);

        List<Thread> threads = new ArrayList<>();
        while (currentPaginationPageUrl.isPresent()) {
            goToRelativeUrl(currentPaginationPageUrl.get());
            threads.addAll(extractThreadsFromCurrentPage());
            currentPaginationPageUrl = webDriver.findElements(By.cssSelector(".element_pagewidget .right")).stream()
                    .findFirst()
                    .map(this::getNextPageUrlFromPagination);
        }

        for (var thread : threads) {
            thread.setPosts(postsExtractor.extractThreadPosts(thread.getUrl()));
        }

        return threads;
    }

    private List<Thread> extractThreadsFromCurrentPage() {
        List<Thread> threads = new ArrayList<>();
        for (var threadElement : webDriver.findElements(By.cssSelector(".row.normal .c.thread"))) {
            var thread = new Thread();
            var threadNameElement = threadElement.findElement(By.cssSelector(".thread-view"));
            thread.setName(threadNameElement.getText());
            thread.setUrl(threadNameElement.getAttribute("href"));
            thread.setAuthor(threadElement.findElement(By.cssSelector(".element_username")).getText());
            threads.add(thread);
        }
        return threads;
    }

}
