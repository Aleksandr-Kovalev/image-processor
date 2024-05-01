package com.underwaterimageprocessor.imageprocessor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrgImage {

    private String requestDate;
    private String Author; //is the email address or other identifier
    private byte[] imageData;
    private String fileType; //should be in ".jpg" format

}
