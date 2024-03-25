package com.devskiller.library.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ResourceLoader {


    public static Optional<String> getProperty(String name)  {
        Properties configuration = new Properties();
        InputStream inputStream = ResourceLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
        try {
            configuration.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(configuration.getProperty("loan.period.days"));
    }
}