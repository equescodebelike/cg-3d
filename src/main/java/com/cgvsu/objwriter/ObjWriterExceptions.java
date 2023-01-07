package com.cgvsu.objwriter;

public class ObjWriterExceptions extends RuntimeException {
    public ObjWriterExceptions(String errorMessage) {
        super("Error parsing OBJ file: " + errorMessage);
    }
}
