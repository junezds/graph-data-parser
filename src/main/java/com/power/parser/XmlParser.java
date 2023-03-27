package com.power.parser;

import com.power.enums.XmlSonNodeType;
import com.power.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlParser {
    public Map<String, String> getAllPSRTypeFromXmlFiles(Stream<Path> xmlFilesStream) {
        final Map<String, String> resultPSRTypeMap = new HashMap<>();

        xmlFilesStream.filter(Files::isRegularFile).forEach(filePath -> {
            File file = filePath.toFile();
            Document document = null;
            try {
                document = XmlUtil.parse(file);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            Element root = document.getRootElement();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
                Element element = it.next();
                if ("PSRType".equals(element.getName())) {
                    String mRID = "";
                    String name = "";
                    for (Iterator<Element> typeIt = element.elementIterator(); typeIt.hasNext();) {
                        Element innerElement = typeIt.next();
                        if ("IdentifiedObject.mRID".equals(innerElement.getName())) {
                            mRID = innerElement.getTextTrim();
                        } else {
                            name = innerElement.getTextTrim();
                        }
                    }
                    resultPSRTypeMap.put(mRID, name);
                }
            }
        });

        return resultPSRTypeMap;
    }

    public Set<String> getAllSonNodeName(Stream<Path> xmlFilesStream) {
        Set<String> sonNodeNameSet = new LinkedHashSet<>();

        xmlFilesStream.filter(Files::isRegularFile).forEach(filePath -> {
            File file = filePath.toFile();
            Document document = null;
            try {
                document = XmlUtil.parse(file);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            Element root = document.getRootElement();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
                Element element = it.next();
                String name = element.getName();
                sonNodeNameSet.add(name);
            }
        });

        return sonNodeNameSet;
    }


    public List<Element> getSonNodeByName(File xmlFile, XmlSonNodeType nodeType) {
        Document document = null;
        try {
            document = XmlUtil.parse(xmlFile);
            return XmlUtil.getElementListByXPath(document,
                    String.format("/rdf:RDF/%s", nodeType.getFullNameWithNameSpace()));
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
