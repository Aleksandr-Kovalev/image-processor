package com.underwaterimageprocessor.imageprocessor.exceptions;

public class ImageNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;
    public ImageNotFoundException(String errors){
        super(errors);
    }
}
