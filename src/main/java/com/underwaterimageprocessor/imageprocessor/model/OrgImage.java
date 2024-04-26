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
@Setter
@Getter
public class OrgImage {

    private Date requestDate;
    private String Author;
    private byte[] imageData;

}
