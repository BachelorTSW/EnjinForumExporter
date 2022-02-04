package com.cerberussolutions.enjin.forum.exporter;

import com.cerberussolutions.enjin.forum.exporter.dto.EnjinSite;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class XmlExporter {

    public void export(EnjinSite enjinSite) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            xmlMapper.writeValue(new File("enjin-forum.xml"), enjinSite);
        } catch (IOException ignored) {}
    }

}
