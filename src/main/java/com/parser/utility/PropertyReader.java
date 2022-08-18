package com.parser.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static Properties valueMap = new Properties();

    public PropertyReader() {
    }

    public static void valueMap(String filename) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream(filename);
            if (inputStream == null) {
                File f = new File(filename);
                f = f.exists() ? f : new File(System.getProperty("user.dir") + File.separator + filename);
                inputStream = new FileInputStream(f);
            }

            valueMap.load((InputStream)inputStream);
            ((InputStream)inputStream).close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static String getFieldValue(String fieldName) {
        if (valueMap.isEmpty()) {
            valueMap("src"+File.separator+"main"+File.separator+"resources"+File.separator+"Configuration.properties");
        }

        //LOG.info("Property Name Received: " + fieldName);
        String fieldValue = valueMap.getProperty(fieldName);
        //LOG.info("Property Value Found: " + fieldValue);
        return fieldValue;
    }


}


