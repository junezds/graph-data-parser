package com.power.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.power.enums.XmlSonNodeType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PSRTypeUtil {
    private static Map<String, PSRTypeInfo> typeCodeInfoMap = new HashMap<>();

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = PSRTypeUtil.class.getClassLoader().getResourceAsStream("PSRTypeMap.json");
        try {
            typeCodeInfoMap = objectMapper.readValue(inputStream, typeCodeInfoMap.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(typeCodeInfoMap);
    }

    public static boolean isDefined(String targetCode) {
        return typeCodeInfoMap.containsKey(targetCode);
    }


    public static String getDescByCode(String code) {
        PSRTypeInfo psrTypeInfo = typeCodeInfoMap.get(code);
        if (psrTypeInfo == null) {
            return "Not Defined Type: " + code;
        } else {
            return psrTypeInfo.getDesc();
        }
    }

    public static XmlSonNodeType getXmlSonNodeTypeByCode(String code) {
        PSRTypeInfo psrTypeInfo = typeCodeInfoMap.get(code);
        if (psrTypeInfo == null) {
            return null;
        } else {
            return psrTypeInfo.getXmlSonNodeType();
        }
    }



    static class PSRTypeInfo {
        private String desc;
        private XmlSonNodeType xmlSonNodeType;

        public PSRTypeInfo(String desc, XmlSonNodeType xmlSonNodeType) {
            this.desc = desc;
            this.xmlSonNodeType = xmlSonNodeType;
        }

        public String getDesc() {
            return desc;
        }

        public XmlSonNodeType getXmlSonNodeType() {
            return xmlSonNodeType;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setXmlSonNodeType(XmlSonNodeType xmlSonNodeType) {
            this.xmlSonNodeType = xmlSonNodeType;
        }
    }
}
