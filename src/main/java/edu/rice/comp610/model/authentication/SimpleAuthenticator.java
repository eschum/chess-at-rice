package edu.rice.comp610.model.authentication;

import com.google.gson.JsonObject;
import java.util.HashMap;

/**
 * Class: Authenticator
 * Implements the IAuthenticate Interface.
 * Design Pattern: Singleton.
 * This class initializes a series of credentials, and facilitates
 * verification as to whether credentials provided match or not.
 */
public class SimpleAuthenticator implements IAuthenticate {
    private static SimpleAuthenticator ref;
    private HashMap<String, String> credentials;

    /**
     * Private Constructor
     * Implements the Singleton design pattern.
     */
    private SimpleAuthenticator() {
        initCredentials();
    }

    /**
     * Method: Get Instance
     * Implements the Singleton design pattern by returning the static / single instance of the class
     * @return The Static instance of the Authenticator class
     */
    public static IAuthenticate getInstance() {
        if (ref == null) ref = new SimpleAuthenticator();
        return ref;
    }

    /**
     * Function: Init Credentials
     * Hard-code a set number of access credentials to test
     * authentication. In the future, could be expanded to a database.
     */
    public void initCredentials() {
        credentials = new HashMap<>();
        credentials.put("eric", "ricestudent");
        credentials.put("zoran", "ricefaculty");
        credentials.put("testaccount", "test-account-pw");
    }

    /**
     * Method: Validate Credentials
     * Check if the username and password are within the credential database
     * @param username User's username string.
     * @param password User's password string.
     * @return A JSONObject that contains whether validation was correct or not.
     * (property "auth" will be set to "true" or "false"). The view will
     * proceed accordingly.
     */
    public JsonObject validateCredentials(String username, String password) {
        JsonObject response = new JsonObject();

        //Return response based on whether or not the ID / password is correct
        if (credentials.containsKey(username) &&
                credentials.get(username).equals(password)) {
            response.addProperty("auth", "true");

        } else {
            response.addProperty("auth", "false");
        }
        System.out.print(true);

        return response;
    }
}
