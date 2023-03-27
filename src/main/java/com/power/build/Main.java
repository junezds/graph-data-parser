package com.power.build;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.power.enums.LayerType;
import com.power.enums.XmlSonNodeType;
import com.power.parser.SvgParser;
import com.power.parser.XmlParser;
import com.power.util.PropertyFileUtil;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        printSize();
//        printContent();

    }

    private static void printSize() throws IOException {
        Properties properties = new PropertyFileUtil().loadPropertyFile("staticFilePath.properties");
        String xmlBasePath = properties.getProperty("xmlBasePath");
        String svgBasePath = properties.getProperty("svgBasePath");
        XmlParser xmlParser = new XmlParser();
        SvgParser svgParser = new SvgParser();

        Stream<Path> pathStream = Files.walk(Path.of(xmlBasePath), 1).filter(Files::isRegularFile);
        pathStream.forEach(xmlFilePath -> {
            String fileName = xmlFilePath.getFileName().toString().replaceFirst(".xml", ".svg");
            Path svgPath = Paths.get(svgBasePath, fileName);
            File svgFile = svgPath.toFile();
            File xmlFile = xmlFilePath.toFile();

            List<Element> xmlElementList = xmlParser.getSonNodeByName(xmlFile, XmlSonNodeType.ACLineSegment);
            List<Element> svgElementList = svgParser.getLayerSonNodeByLayerName(svgFile, LayerType.ACLineSegment_Layer);

            System.out.println(String.format("xml:%d; svg:%d; %s", xmlElementList.size(), svgElementList.size(), fileName));
        });
    }

    private static void printContent() {
        String xmlUrl = "/home/neon/BigProjects/bigData/power_grid/xml/4c40180b-acb9-47c5-9615-d5bf434920f3__10kV吴家山线133.xml";
        String svgUrl = "/home/neon/BigProjects/bigData/power_grid/svg/4c40180b-acb9-47c5-9615-d5bf434920f3__10kV吴家山线133.svg";
        File xmlFile = new File(xmlUrl);
        File svgFile = new File(svgUrl);
        XmlParser xmlParser = new XmlParser();
        SvgParser svgParser = new SvgParser();
        List<Element> xmlElementList = xmlParser.getSonNodeByName(xmlFile, XmlSonNodeType.PoleCode);
        List<Element> svgElementList = svgParser.getLayerSonNodeByLayerName(svgFile, LayerType.PoleCode_Layer);

        xmlElementList.forEach(element -> {
            for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();) {
                System.out.println(it.next().getValue());
            }
        });

        System.out.println();

        svgElementList.forEach(element -> {
            for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();) {
                System.out.println(it.next().getValue());
            }
        });
    }

    /**
     * 获取所有的XML中的PSRType
     * 结果：{"10000100":"馈线","30000005":"配电站","30200002":"配电变压器-双绕组","30500000":"站内-断路器","30000004":"开关站","10200000":"直线-运行杆塔","20200000":"站外-电缆终端头","10200001":"耐张-运行杆塔","11300000":"柱上-隔离开关","32400000":"环网柜","37000000":"站外-中压用户接入点","20100000":"站外-电缆段","10100000":"导线段","30700002":"站内-三工位负荷开关","11200000":"柱上-负荷开关","20400000":"站房-电缆分支箱","32300000":"箱式变电站","30700000":"站内-负荷开关","30500099":"配电-站内三工位断路器","11500001":"柱上-负荷式熔断器","11500000":"柱上-跌落式熔断器","11000000":"柱上变压器","11000001":"柱上-用户变压器","30600000":"站内-隔离开关","30900000":"站内-熔断器","30300000":"站内-所用变","30000000":"变电站","31100000":"母线","30600007":"站内-三工位隔离刀闸","30600006":"站内-T型开关","20300000":"站外-电缆终端头","11100000":"柱上-断路器","11400000":"柱上-重合器","30600002":"站内-V型开关","30600001":"站内-接地刀闸"}
     * @throws IOException
     */
    public static void getAllPSRTypesJson() throws IOException {
        Properties properties = new PropertyFileUtil().loadPropertyFile("staticFilePath.properties");
        String basePath =  properties.getProperty("xmlBasePath");

        XmlParser xmlParser = new XmlParser();
        Map<String, String> psrTypeMap = xmlParser.getAllPSRTypeFromXmlFiles(
                Files.walk(Path.of(basePath),1));

        ObjectMapper objectMapper = new ObjectMapper();
        String resultString = objectMapper.writeValueAsString(psrTypeMap);
        System.out.println(resultString);
    }


    /**
     * 获取根节点下所有XML子节点类型
     * 结果：["FullModel","PSRType","GeographicalRegion","SubGeographicalRegion","Asset","Substation","BaseVoltage","Breaker","ConnectivityNode","Terminal","AssetDeployment","Feeder","ACLineSegment","Junction","BusbarSection","CompositeSwitch","LoadBreakSwitch","GroundDisconnector","EnergyConsumer","UsagePoint","VoltageLevel","Organisation","AssetUser","ParentOrganization","Maintainer","PowerTransformer","PowerTransformerEnd","Fuse","PoleCode","Disconnector"]
     * @throws IOException
     */
    public static void getAllXmlSonNodeNamesJson() throws IOException {
        Properties properties = new PropertyFileUtil().loadPropertyFile("staticFilePath.properties");
        String basePath =  properties.getProperty("xmlBasePath");

        XmlParser xmlParser = new XmlParser();
        Set<String> sonNodeNamesSet = xmlParser.getAllSonNodeName(
                Files.walk(Path.of(basePath), 1));

        ObjectMapper objectMapper = new ObjectMapper();
        String resultString = objectMapper.writeValueAsString(sonNodeNamesSet);
        System.out.println(resultString);
    }


    /**
     * 获取SVG的所有layerName
     * 结果：[null,"BackGround_Layer","Substation_Layer","ConnLine_Layer","ACLineSegment_Layer","BusbarSection_Layer","Junction_Layer","Breaker_Layer","CompositeSwitch_Layer","EnergyConsumer_Layer","Text_Layer","PowerTransformer_Layer","LoadBreakSwitch_Layer","PoleCode_Layer","Other_Layer","Fuse_Layer","Disconnector_Layer"]
     * @throws IOException
     */
    public static void getAllSvgGLayerNamesJson() throws IOException {
        Properties properties = new PropertyFileUtil().loadPropertyFile("staticFilePath.properties");
        String basePath =  properties.getProperty("svgBasePath");

        SvgParser svgParser = new SvgParser();
        Set<String> gLayerNamesSet = svgParser.getAllGLayerName(
                Files.walk(Path.of(basePath), 1));

        ObjectMapper objectMapper = new ObjectMapper();
        String resultString = objectMapper.writeValueAsString(gLayerNamesSet);
        System.out.println(resultString);
    }

}
