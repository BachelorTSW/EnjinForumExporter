package com.cerberussolutions.enjin.forum.exporter.extractor;

import com.cerberussolutions.enjin.forum.exporter.dto.Post;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostsExtractor extends AbstractExtractor {

    public PostsExtractor(WebDriver webDriver) {
        super(webDriver);
    }

    public List<Post> extractThreadPosts(String threadUrl) {
        Optional<String> currentPaginationPageUrl = Optional.ofNullable(threadUrl);

        List<Post> posts = new ArrayList<>();
        while (currentPaginationPageUrl.isPresent()) {
            goToRelativeUrl(currentPaginationPageUrl.get());
            posts.addAll(extractPostsFromCurrentPage());
            currentPaginationPageUrl = webDriver.findElements(By.cssSelector(".element_pagewidget .right")).stream()
                    .findFirst()
                    .map(this::getNextPageUrlFromPagination);
        }
        return posts;
    }

    private List<Post> extractPostsFromCurrentPage() {
        List<Post> posts = new ArrayList<>();
        for (var postElement : webDriver.findElements(By.xpath("//*[@post_id]"))) {
            var post = new Post();
            post.setAuthor(postElement.findElement(By.cssSelector(".element_username")).getText());
            post.setContent(postElement.findElement(By.cssSelector(".post-content")).getAttribute("innerHTML"));
            posts.add(post);
        }
        return posts;
    }

}
