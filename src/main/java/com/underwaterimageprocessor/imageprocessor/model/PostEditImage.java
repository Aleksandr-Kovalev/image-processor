package com.underwaterimageprocessor.imageprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostEditImage {

    private String processDate;
    private String Author;
    private String editAuthor;
    private BufferedImage image;
    private Map<String, String> metaData;

}
