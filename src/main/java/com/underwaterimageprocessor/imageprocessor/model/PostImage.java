package com.underwaterimageprocessor.imageprocessor.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class PostImage {

    private Date processDate;
    private String Author;
    private String editAuthor;
    private BufferedImage image;
    private Map<String, String> metaData;

}
