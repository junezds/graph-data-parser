package com.power.parser;

import com.power.enums.LayerType;
import com.power.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SvgParser {
    public Set<String> getAllGLayerName(Stream<Path> xmlFilesStream) {
        Set<String> gLayerNameSet = new LinkedHashSet<>();

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
                String layerName = element.attributeValue("id");
                gLayerNameSet.add(layerName);
            }
        });

        return gLayerNameSet;
    }


    public List<Element> getLayerSonNodeByLayerName(File svgFile, LayerType layerType) {
        Document document = null;
        try {
            document = XmlUtil.parse(svgFile);
            return XmlUtil.getElementListByXPath(document,
                    String.format("/*[name()=\"svg\"]/*[name()=\"g\" and @id=\"%s\"]/*[name()=\"g\"]", layerType.getValue())
//                    "/svg/g"
            );
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

}
