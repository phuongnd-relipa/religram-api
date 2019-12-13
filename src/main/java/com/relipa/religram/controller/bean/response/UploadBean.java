package com.relipa.religram.controller.bean.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadBean {
    private String fileName;
    private String fileUri;
    private String fileType;
}