package com.underwaterimageprocessor.imageprocessor.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private int errorCode;
    private String message;

}
