package edu.rice.comp610.model.validation;

import com.google.gson.JsonObject;

/**
 * Interface IAuthenticate
 * This interface defines the behavior that the authentication class should
 * have.
 */
public interface IAuthenticate {
    /**
     * Method: Init Credentials
     * Perform the initial configuration of the credentials.
     * May be a database instantiation, etc.
     */
    void initCredentials();

    /**
     * Method: Validate Credentials
     * Take a String of a username and a password. Return value based
     * on whether or not validation has passed.
     * @param username String of the username of the user.
     * @param password String of the password of the user.
     * @return Json object with a property "auth", that will either have
     * the value "true" or "false" based on the validity of the credentials.
     */
    JsonObject validateCredentials(String username, String password);
}
