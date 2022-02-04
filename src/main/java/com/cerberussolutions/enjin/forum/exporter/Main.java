package com.cerberussolutions.enjin.forum.exporter;

public class Main {

    public static void main(String[] args) {
        var enjinForumCrawler = new EnjinForumCrawler();
        var enjinSite = enjinForumCrawler.getForumPosts();

        var xmlExporter = new XmlExporter();
        xmlExporter.export(enjinSite);
    }

}
