package com.cerberussolutions.enjin.forum.exporter.extractor;

import com.cerberussolutions.enjin.forum.exporter.dto.Forum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class ForumsExtractor extends AbstractExtractor {

    private final ThreadsExtractor threadsExtractor;

    public ForumsExtractor(WebDriver webDriver) {
        super(webDriver);
        this.threadsExtractor = new ThreadsExtractor(webDriver);
    }

    public List<Forum> extractForums() {
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
            goToRelativeUrl(forum.getUrl());
            forum.setSubForums(extractForums());
            forum.setThreads(threadsExtractor.extractForumThreads(forum.getUrl()));
        }

        return forums;
    }

}
