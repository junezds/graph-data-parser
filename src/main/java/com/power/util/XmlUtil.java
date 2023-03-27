package com.power.util;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlUtil {
    public static void main(String[] args) throws DocumentException {
//        String url = "/home/neon/BigProjects/bigData/power_grid/xml/10DKX-208049__10kV小粉桥#2线132.xml";
        File file = new File("/home/neon/BigProjects/bigData/power_grid/svg/10DKX-208049__10kV小粉桥#2线132.svg");
        Document document = parse(file);
        System.out.println(document.selectNodes("/*[name()=\"svg\"]/*[name()=\"g\"]"));

    }

    public static Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }

    public static List<Element> getElementListByXPath(Document document, String XpathStr) {
        List<Element> elementList = new ArrayList<>();

        try {
            elementList = document.selectNodes(XpathStr)
                    .stream()
                    .filter(node -> node instanceof Element)
                    .map(node -> (Element)node)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(document);
            e.printStackTrace();
        }

        return elementList;
    }

    /**
     * Using Iterators:
     *
     * A document can be navigated using a variety of methods that return standard Java Iterators.
     */
    private static void bar(Document document) throws DocumentException {

        Element root = document.getRootElement();

        // iterate through child elements of root
        for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
            Element element = it.next();
            // do something
        }

        // iterate through child elements of root with element name "foo"
        for (Iterator<Element> it = root.elementIterator("foo"); it.hasNext();) {
            Element foo = it.next();
            // do something
        }

        // iterate through attributes of root
        for (Iterator<Attribute> it = root.attributeIterator(); it.hasNext();) {
            Attribute attribute = it.next();
            // do something
        }
    }


    /**
     * Powerful Navigation with XPath:
     *
     * In <dom4j> XPath expressions can be evaluated on the Document or on any Node in the tree
     * (such as Attribute, Element or ProcessingInstruction).
     * This allows complex navigation throughout the document with a single line of code.
     */
    private static void barXPath(Document document) {
        List<Node> list = document.selectNodes("//foo/bar");

        Node node = document.selectSingleNode("//foo/bar/author");

        String name = node.valueOf("@name");
    }


    /**
     * Fast Looping:
     *
     * If you ever have to walk a large XML document tree then for performance
     * we recommend you use the fast looping method
     * which avoids the cost of creating an Iterator object for each loop.
     */
    private static void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    private static void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                treeWalk((Element) node);
            }
            else {
                // do something…
            }
        }
    }
}
