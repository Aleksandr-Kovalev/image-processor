package com.underwaterimageprocessor.imageprocessor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;


import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;
import java.util.Map;

@Document(collection = "org_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrgImage {

    @Id
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id; //image id
    @DocumentReference
    private String authorId;
    private String name;
    private String fileType;      //should be in ".jpg" format
    private byte[] image;
    private String byteURI;
    private String uploadDate;
    private JSONObject metaData;


}
