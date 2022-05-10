package com.example.csan.model.connection;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.example.csan.model.connection.DatabasePropertyKey.*;

class DatabasePropertyReader {


    private static final String DATABASE_PROPERTY_FILE = "database";


    static final String DATABASE_DRIVER;


    static final String DATABASE_USERNAME;


    static final String DATABASE_PASSWORD;


    static final int CONNECTION_POOL_SIZE;


    static final String DATABASE_URL;

    static {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTY_FILE);
            DATABASE_DRIVER = resourceBundle.getString(DATABASE_DRIVER_KEY);
            DATABASE_USERNAME = resourceBundle.getString(DATABASE_USER_KEY);
            DATABASE_PASSWORD = resourceBundle.getString(DATABASE_PASSWORD_KEY);
            CONNECTION_POOL_SIZE = Integer.parseInt(resourceBundle.getString(DATABASE_POOL_SIZE_KEY));
            DATABASE_URL = resourceBundle.getString(DATABASE_URL_KEY);

            System.out.println(DATABASE_DRIVER + " " + DATABASE_USERNAME + " " + DATABASE_PASSWORD + " " + CONNECTION_POOL_SIZE + " " + DATABASE_URL);
        } catch (MissingResourceException e) {
            throw new RuntimeException("File or key is not found", e);
        }
    }
}