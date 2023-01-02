package com.cgvsu.objwriter;

public class ObjWriterException extends RuntimeException {
    public ObjWriterException(String errorMessage) {
        super("Error parsing OBJ file: " + errorMessage); // lineInd?
    }
}
