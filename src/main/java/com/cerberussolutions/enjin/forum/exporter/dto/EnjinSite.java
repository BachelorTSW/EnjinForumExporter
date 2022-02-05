package com.cerberussolutions.enjin.forum.exporter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
