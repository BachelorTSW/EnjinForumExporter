package com.cerberussolutions.enjin.forum.exporter.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class EnjinSite {

    @JacksonXmlElementWrapper(localName = "forums")
    @JacksonXmlProperty(localName = "forum")
    private List<Forum> forums;

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }
}
