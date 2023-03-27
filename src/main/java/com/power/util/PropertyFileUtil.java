package com.power.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileUtil {
    public Properties loadPropertyFile(String pathFromClasspath) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("staticFilePath.properties");
        properties.load(inputStream);
        return properties;
    }
}
