package org.DVitushkin.customexception;

public class MachineException extends Exception{
    public MachineException(String message) {
        super(String.format("[Machine Exception]\n%s", message));
    }
}

