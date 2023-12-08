package org.DVitushkin.customexception;

public class ProfileException extends Exception{
    public ProfileException(String message) {
        super(String.format("[Profile Exception]\n%s", message));
    }
}

